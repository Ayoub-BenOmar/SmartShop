package org.example.smartshop.model.dto;

import lombok.*;
import org.example.smartshop.enums.UserRole;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String username;
    private UserRole role;
    private String name;
    private String email;
    private String loyaltyLevel;
    private List<Integer> commandeIds;
}
