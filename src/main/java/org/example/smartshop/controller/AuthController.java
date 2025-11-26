package org.example.smartshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.mapper.UserMapper;
import org.example.smartshop.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest request) {
        return authService.authenticate(req.username(), req.password())
                .map(user -> {
                    HttpSession old = request.getSession(false);
                    if (old != null) old.invalidate();

                    HttpSession session = request.getSession(true);
                    session.setAttribute("USER_ID", user.getId());
                    session.setAttribute("USER_ROLE", user.getRole().name());

                    UserDto dto = UserMapper.toDto(user);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}
