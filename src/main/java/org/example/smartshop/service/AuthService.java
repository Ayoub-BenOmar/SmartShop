package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.repository.UserRepository;
import org.example.smartshop.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public Optional<User> authenticate(String username, String password) {
        if (username == null) username = "";
        String trimmed = username.trim();
        Optional<User> userOpt = userRepo.findByUsername(trimmed);
        if (userOpt.isEmpty()) {
            userOpt = userRepo.findByUsernameIgnoreCase(trimmed);
        }
        if (userOpt.isEmpty()) {
            log.info("Authentication failed: user not found for username='{}'", trimmed);
            return Optional.empty();
        }
        User user = userOpt.get();
        boolean ok = PasswordUtil.verify(password, user.getPassword());
        if (!ok) {
            log.info("Authentication failed: password mismatch for username='{}'", trimmed);
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
