package org.example.smartshop.model.mapper;

import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto dto);
}
