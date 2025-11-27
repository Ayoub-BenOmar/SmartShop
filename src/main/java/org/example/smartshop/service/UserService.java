package org.example.smartshop.service;

import org.example.smartshop.enums.UserRole;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.model.mapper.UserMapper;
import org.example.smartshop.repository.UserRepository;
import org.example.smartshop.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto create(UserDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setRole(UserRole.CLIENT);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto update(Integer id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(dto.getUsername());
        existing.setRole(dto.getRole());
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setLoyaltyLevel(dto.getLoyaltyLevel());

        User updated = userRepository.save(existing);
        return userMapper.toDto(updated);
    }

    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDto getById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
