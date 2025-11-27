package org.example.smartshop.model.mapper;

import org.example.smartshop.model.dto.CommandeDto;
import org.example.smartshop.model.entity.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {

    CommandeDto toDto(Commande commande);
    Commande toEntity(CommandeDto dto);
}
