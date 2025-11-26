package org.example.smartshop.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}