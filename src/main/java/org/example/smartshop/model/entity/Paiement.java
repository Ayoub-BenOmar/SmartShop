package org.example.smartshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartshop.enums.PaymentMethode;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(nullable = false)
    private Integer paymentNumber;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethode payementMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    private LocalDateTime collectionDate;

}

