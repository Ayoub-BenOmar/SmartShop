package org.example.smartshop.model.mapper;

import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);
    Product toEntity(ProductDto dto);
}
