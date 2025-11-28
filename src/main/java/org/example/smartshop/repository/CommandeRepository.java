package org.example.smartshop.repository;

import org.example.smartshop.model.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findByClientId(Integer clientId);
}
