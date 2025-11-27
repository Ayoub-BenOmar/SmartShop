package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.repository.UserRepository;
import org.example.smartshop.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;

    public Optional<User> authenticate(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> PasswordUtil.verify(password, u.getPassword()));
    }
}
