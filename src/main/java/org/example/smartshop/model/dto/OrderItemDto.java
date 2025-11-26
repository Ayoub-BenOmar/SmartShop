package org.example.smartshop.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Integer id;
    private Integer commandeId;
    private Integer productId;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
}
