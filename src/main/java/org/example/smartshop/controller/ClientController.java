package org.example.smartshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.service.CommandeService;
import org.example.smartshop.service.ProductService;
import org.example.smartshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final UserService userService;
    private final CommandeService commandeService;
    private final ProductService productService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        Integer userId = getSessionUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        UserDto dto = userService.getProfileForClient(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        Integer userId = getSessionUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        List<CommandeDto> orders = userService.getOrdersForClient(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpServletRequest request) {
        Integer userId = getSessionUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Map<String, Object> stats = userService.getStatsForClient(userId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/products")
    public ResponseEntity<?> listProducts() {
        List<ProductDto> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    private Integer getSessionUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object id = session.getAttribute("USER_ID");
        if (id instanceof Integer) return (Integer) id;
        if (id instanceof Number) return ((Number) id).intValue();
        return null;
    }
}
