package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.entity.Commande;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.model.mapper.CommandeMapper;
import org.example.smartshop.model.mapper.UserMapper;
import org.example.smartshop.repository.CommandeRepository;
import org.example.smartshop.repository.UserRepository;
import org.example.smartshop.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    public UserDto create(UserDto dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }

        dto.setUsername(dto.getUsername().trim());

        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hash(dto.getPassword()));
        user.setRole(UserRole.CLIENT);
        user.setLoyaltyLevel(CustomerTier.BASIC);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto update(Integer id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(dto.getUsername());
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());

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

    public double calculateTotalSpent(User client) {
        User user = userRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCommandes()
                .stream()
                .mapToDouble(commande -> commande.getTotal() != null ? commande.getTotal() : 0.0)
                .sum();
    }

    public int calculateTotalOrders(User client) {
        User user = userRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCommandes().size();
    }

    // --- New business methods for client functionality ---

    public UserDto getProfileForClient(Integer clientId) {
        return getById(clientId);
    }

    public List<CommandeDto> getOrdersForClient(Integer clientId) {
        List<Commande> commandes = commandeRepository.findByClientId(clientId);
        return commandes.stream()
                .map(commandeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getStatsForClient(Integer clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        int totalOrders = calculateTotalOrders(user);
        double totalSpent = calculateTotalSpent(user);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("totalSpent", totalSpent);
        return stats;
    }
}
