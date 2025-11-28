package org.example.smartshop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.UserRole;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserRole role;
    private String name;
    private String email;
    private CustomerTier loyaltyLevel;
    private List<Integer> commandeIds;
}
