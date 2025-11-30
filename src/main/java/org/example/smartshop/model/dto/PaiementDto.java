package org.example.smartshop.model.dto;

import lombok.*;
import org.example.smartshop.enums.PaymentMethode;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDto {

    private Integer id;
    private Integer commandeId;
    private Integer paymentNumber;
    private Double montant;
    private PaymentMethode payementMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime collectionDate;
}
