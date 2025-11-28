package org.example.smartshop.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeItemDto {
    private Integer productId;
    private Integer quantity;
}
