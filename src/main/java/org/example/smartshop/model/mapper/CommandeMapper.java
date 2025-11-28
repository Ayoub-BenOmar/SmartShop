package org.example.smartshop.model.mapper;

import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.dto.CommandeItemDto;
import org.example.smartshop.model.entity.Commande;
import org.example.smartshop.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "items", target = "items")
    CommandeDto toDto(Commande commande);
    Commande toEntity(CommandeDto dto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    CommandeItemDto orderItemToDto(OrderItem item);
}
