package org.example.smartshop.service;

import lombok.*;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.entity.Commande;
import org.example.smartshop.model.entity.OrderItem;
import org.example.smartshop.model.entity.Paiement;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.model.mapper.CommandeMapper;
import org.example.smartshop.repository.CommandeRepository;
import org.example.smartshop.repository.OrderItemRepository;
import org.example.smartshop.repository.PaiementRepository;
import org.example.smartshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaiementRepository paiementRepository;

    public CommandeDto create(CommandeDto dto) {
        Commande commande = new Commande();

        User client = userRepository.findById(dto.getClientId()).orElseThrow(() -> new RuntimeException("Client not found"));
        commande.setClient(client);

        List<OrderItem> items = orderItemRepository.findAllById(dto.getItemIds());
        commande.setItems(items);

        List<Paiement> payments = paiementRepository.findAllById(dto.getPaiementIds());
        commande.setPayements(payments);

        commande.setDate(dto.getDate());
        commande.setSubtotal(dto.getSubtotal());
        commande.setDiscount(dto.getDiscount());
        commande.setTva(dto.getTva());
        commande.setTotal(dto.getTotal());
        commande.setPromotionCode(dto.getPromotionCode());
        commande.setStatut(dto.getStatut());
        commande.setRemainingAmount(dto.getRemainingAmount());

        commande = commandeRepository.save(commande);

        return commandeMapper.toDto(commande);
    }

    public CommandeDto getById(Integer id) {
        Commande commande = commandeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commande not found"));
        return commandeMapper.toDto(commande);
    }

    public List<CommandeDto> getAll() {
        return commandeRepository.findAll()
                .stream()
                .map(commandeMapper::toDto)
                .toList();
    }

    public CommandeDto update(Integer id, CommandeDto dto) {

        Commande existing = commandeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commande not found"));

        User client = userRepository.findById(dto.getClientId()).orElseThrow(() -> new RuntimeException("Client not found"));
        existing.setClient(client);

        List<OrderItem> items = orderItemRepository.findAllById(dto.getItemIds());
        existing.setItems(items);

        List<Paiement> payments = paiementRepository.findAllById(dto.getPaiementIds());
        existing.setPayements(payments);

        existing.setSubtotal(dto.getSubtotal());
        existing.setDiscount(dto.getDiscount());
        existing.setTva(dto.getTva());
        existing.setTotal(dto.getTotal());
        existing.setPromotionCode(dto.getPromotionCode());
        existing.setStatut(dto.getStatut());
        existing.setRemainingAmount(dto.getRemainingAmount());

        Commande updated = commandeRepository.save(existing);

        return commandeMapper.toDto(updated);
    }

    public void delete(Integer id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande not found");
        }
        commandeRepository.deleteById(id);
    }

    public CommandeDto updateStatus(Integer id, OrderStatus status) {
        Commande existing = commandeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commande not found"));
        existing.setStatut(status);
        Commande updated = commandeRepository.save(existing);
        return commandeMapper.toDto(updated);
    }
}
