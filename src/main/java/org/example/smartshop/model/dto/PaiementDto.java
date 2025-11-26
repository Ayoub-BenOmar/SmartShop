package org.example.smartshop.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDto {

    private Integer id;
    private Integer commandeId;
    private Integer paymentNumber;
    private Double montant;
    private String payementMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime collectionDate;
}
