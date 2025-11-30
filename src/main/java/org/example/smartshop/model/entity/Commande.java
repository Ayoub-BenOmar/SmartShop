package org.example.smartshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(nullable = false)
    private LocalDateTime date;

    private Double subtotal;
    private Double discount;
    private Double tva;
    private Double total;
    private String promotionCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut;

    private Double remainingAmount;

}
