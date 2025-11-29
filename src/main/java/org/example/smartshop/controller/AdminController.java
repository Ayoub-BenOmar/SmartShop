package org.example.smartshop.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.exceptions.ForbiddenException;
import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.service.CommandeService;
import org.example.smartshop.service.ProductService;
import org.example.smartshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final CommandeService commandeService;

    private void checkAdmin(HttpSession session) {
        Object roleObj = session.getAttribute("USER_ROLE");
        if (roleObj == null || roleObj != UserRole.ADMIN) {
            throw new ForbiddenException("Forbidden: Admin access required");
        }
    }

    // ----- User management -----
    @PostMapping("/create-client")
    public ResponseEntity<?> createClient(@RequestBody UserDto dto, HttpSession session) {
        checkAdmin(session);
        UserDto created = userService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(HttpSession session){
        checkAdmin(session);
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id, HttpSession session){
        checkAdmin(session);
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id, HttpSession session){
        checkAdmin(session);
        userService.delete(id);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable Integer id, @RequestBody UserDto dto, HttpSession session){
        checkAdmin(session);
        userService.update(id, dto);
    }


    // ----- Product management -----
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto dto, HttpSession session){
        checkAdmin(session);
        ProductDto created = productService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto dto, HttpSession session){
        checkAdmin(session);
        ProductDto updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, HttpSession session){
        checkAdmin(session);
        productService.delete(id);
        return ResponseEntity.ok("Product deleted");
    }


    // ----- Update Order Status -----
    @PutMapping("/commandes/{id}/status")
    public ResponseEntity<CommandeDto> updateCommandeStatus(@PathVariable Integer id, @PathVariable OrderStatus status, HttpSession session){
        checkAdmin(session);
        checkAdmin(session);
        return ResponseEntity.ok(commandeService.updateStatus(id, status));
    }


    // ----- Commande Management -----
    @PostMapping("/create-commande")
    public ResponseEntity<?> createCommande(@RequestBody CommandeDto dto, HttpSession session){
        checkAdmin(session);
        CommandeDto created = commandeService.create(dto);
        return ResponseEntity.ok(created);
    }
}
