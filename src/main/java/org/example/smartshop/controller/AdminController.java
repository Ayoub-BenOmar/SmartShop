package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/create-client")
    public ResponseEntity<?> createClient(@RequestBody UserDto dto, HttpSession session) {

        Object roleObj = session.getAttribute("USER_ROLE");
        if (roleObj == null || roleObj != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Forbidden: Admin only");
        }

        dto.setRole(UserRole.CLIENT);

        UserDto created = userService.create(dto);

        return ResponseEntity.ok(created);
    }
}
