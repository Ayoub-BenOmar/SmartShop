package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.PaymentMethode;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.model.dto.PaiementDto;
import org.example.smartshop.model.entity.Commande;
import org.example.smartshop.model.entity.Paiement;
import org.example.smartshop.repository.CommandeRepository;
import org.example.smartshop.repository.PaiementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;

    public PaiementDto create(PaiementDto dto) {

        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        if (dto.getMontant() <= 0) {
            throw new RuntimeException("Payment amount must be positive");
        }

        if (dto.getMontant() > commande.getRemainingAmount()) {
            throw new RuntimeException("Payment exceeds remaining amount!");
        }

        int existingPayments = paiementRepository.countByCommandeId(dto.getCommandeId());
        int paymentNumber = existingPayments + 1;

        Paiement paiement = new Paiement();
        paiement.setCommande(commande);
        paiement.setPaymentNumber(paymentNumber);
        paiement.setMontant(dto.getMontant());
        paiement.setPayementMethod(dto.getPayementMethod());
        paiement.setPaymentDate(LocalDateTime.now());
        paiement.setCollectionDate(dto.getCollectionDate());

        if (PaymentMethode.CASH.equals(dto.getPayementMethod())) {
            paiement.setPaymentStatus(PaymentStatus.PAID);
        } else {
            paiement.setPaymentStatus(PaymentStatus.PENDING);
        }

        Paiement saved = paiementRepository.save(paiement);

        double remaining = commande.getRemainingAmount() - saved.getMontant();
        remaining = Math.max(0, remaining);
        commande.setRemainingAmount(remaining);

        if (remaining == 0) {
            commande.setStatut(OrderStatus.CONFIRMED);
        }
        commandeRepository.save(commande);

        PaiementDto response = new PaiementDto();
        response.setId(saved.getId());
        response.setCommandeId(commande.getId());
        response.setPaymentNumber(saved.getPaymentNumber());
        response.setMontant(saved.getMontant());
        response.setPayementMethod(saved.getPayementMethod());
        response.setPaymentDate(saved.getPaymentDate());
        response.setCollectionDate(saved.getCollectionDate());

        return response;
    }

}
