package org.example.smartshop.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDto {

    private Integer id;
    private Integer clientId;
    private List<Integer> itemIds;
    private LocalDateTime date;
    private Double subtotal;
    private Double discount;
    private Double tva;
    private Double total;
    private String promotionCode;
    private String statut;
    private Double remainingAmount;
    private List<Integer> paiementIds;
}
