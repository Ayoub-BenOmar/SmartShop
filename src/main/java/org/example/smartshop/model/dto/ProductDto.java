package org.example.smartshop.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private Double unitPrice;
    private Integer availableQuantity;
}
