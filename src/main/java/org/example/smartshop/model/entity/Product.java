package org.example.smartshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private Boolean isVisible = true;
}

