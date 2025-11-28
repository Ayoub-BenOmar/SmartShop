package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
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

    public  PaiementDto create(PaiementDto dto) {

        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        Paiement paiement = new Paiement();
        paiement.setCommande(commande);
        paiement.setPaymentNumber(dto.getPaymentNumber());
        paiement.setMontant(dto.getMontant());
        paiement.setPayementMethod(dto.getPayementMethod());
        paiement.setPaymentDate(LocalDateTime.now());
        paiement.setCollectionDate(dto.getCollectionDate());

        Paiement saved = paiementRepository.save(paiement);

        double remaining = commande.getRemainingAmount() - saved.getMontant();
        commande.setRemainingAmount(Math.max(0, remaining));
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
