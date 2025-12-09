package org.example.smartshop.service;

import lombok.*;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.exceptions.BadRequestException;
import org.example.smartshop.exceptions.NotFoundException;
import org.example.smartshop.exceptions.UnprocessableEntityException;
import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.dto.CommandeItemDto;
import org.example.smartshop.model.entity.*;
import org.example.smartshop.model.mapper.CommandeMapper;
import org.example.smartshop.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OrderItemService orderItemService;

    public CommandeDto create(CommandeDto dto) {

        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BadRequestException("Commande must have at least one item");
        }

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDate(LocalDateTime.now());
        commande.setPromotionCode(dto.getPromotionCode());
        commande.setStatut(OrderStatus.PENDING);

        Commande savedCommande = commandeRepository.save(commande);

        double subtotal = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CommandeItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemDto.getProductId()));

            if (itemDto.getQuantity() > product.getAvailableQuantity()) {
                throw new UnprocessableEntityException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = orderItemService.create(savedCommande, product, itemDto.getQuantity());
            orderItems.add(orderItem);

            product.setAvailableQuantity(product.getAvailableQuantity() - itemDto.getQuantity());
            productRepository.save(product);

            subtotal += orderItem.getLineTotal();
        }

        double discount = 0.0;
        if (client.getLoyaltyLevel() == CustomerTier.SILVER && subtotal >= 500) {
            discount = subtotal * 0.05;
        } else if (client.getLoyaltyLevel() == CustomerTier.GOLD && subtotal >= 800) {
            discount = subtotal * 0.10;
        } else if (client.getLoyaltyLevel() == CustomerTier.PLATINUM && subtotal >= 1200) {
            discount = subtotal * 0.15;
        }

        if (dto.getPromotionCode() != null && dto.getPromotionCode().matches("PROMO-[A-Z0-9]{4}")) {
            discount += subtotal * 0.05;
        }

        double totalHT = subtotal - discount;
        double tva = totalHT * 0.20;
        double totalTTC = totalHT + tva;

        savedCommande.setItems(orderItems);
        savedCommande.setSubtotal(subtotal);
        savedCommande.setDiscount(discount);
        savedCommande.setTva(tva);
        savedCommande.setTotal(totalTTC);
        savedCommande.setRemainingAmount(totalTTC);

        savedCommande = commandeRepository.save(savedCommande);
        updateClientLoyalty(client, totalTTC);

        return commandeMapper.toDto(savedCommande);
    }

    private void updateClientLoyalty(User client, double lastCommandeAmount) {
        int orders = userService.calculateTotalOrders(client) + 1;
        double spent = userService.calculateTotalSpent(client) + lastCommandeAmount;

        if (orders >= 20 || spent >= 15000) client.setLoyaltyLevel(CustomerTier.PLATINUM);
        else if (orders >= 10 || spent >= 5000) client.setLoyaltyLevel(CustomerTier.GOLD);
        else if (orders >= 3 || spent >= 1000) client.setLoyaltyLevel(CustomerTier.SILVER);
        else client.setLoyaltyLevel(CustomerTier.BASIC);

        userRepository.save(client);
    }

    public CommandeDto getById(Integer id) {
        Commande commande = commandeRepository.findById(id).orElseThrow(() -> new NotFoundException("Commande not found"));
        return commandeMapper.toDto(commande);
    }

    public List<CommandeDto> getAll() {
        return commandeRepository.findAll()
                .stream()
                .map(commandeMapper::toDto)
                .toList();
    }

    public void delete(Integer id) {
        if (!commandeRepository.existsById(id)) {
            throw new NotFoundException("Commande not found");
        }
        commandeRepository.deleteById(id);
    }

    public CommandeDto updateStatus(Integer id, OrderStatus status) {
        Commande existing = commandeRepository.findById(id).orElseThrow(() -> new NotFoundException("Commande not found"));
        existing.setStatut(status);
        if (status.equals(OrderStatus.CONFIRMED)) {
            updateClientLoyalty(existing.getClient(), existing.getTotal());
        }
        Commande updated = commandeRepository.save(existing);
        return commandeMapper.toDto(updated);
    }
}
