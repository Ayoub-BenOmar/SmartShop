package org.example.smartshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.dto.LoginRequestDto;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.mapper.UserMapper;
import org.example.smartshop.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto req, HttpServletRequest request) {
        var authOpt = authService.authenticate(req.getUsername(), req.getPassword());
        if (authOpt.isPresent()) {
            var user = authOpt.get();
            HttpSession old = request.getSession(false);
            if (old != null) old.invalidate();

            HttpSession session = request.getSession(true);
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("USER_ROLE", user.getRole().name());

            UserDto dto = userMapper.toDto(user);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}
