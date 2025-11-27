package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.dto.UserDto;
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

    private void checkAdmin(HttpSession session) {
        Object roleObj = session.getAttribute("USER_ROLE");
        if (roleObj == null || roleObj != UserRole.ADMIN) {
            throw new RuntimeException("Forbidden: Admin only");
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


    // ----- Product management -----
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto dto, HttpSession session){
        checkAdmin(session);
        ProductDto created = productService.create(dto);
        return ResponseEntity.ok(created);
    }

}
