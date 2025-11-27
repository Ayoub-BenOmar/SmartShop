package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.model.mapper.ProductMapper;
import org.example.smartshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDto create(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto update(Integer id, ProductDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.getName());
        existing.setUnitPrice(dto.getUnitPrice());
        existing.setAvailableQuantity(dto.getAvailableQuantity());

        Product updated = productRepository.save(existing);
        return productMapper.toDto(updated);
    }

    public void delete(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setVisible(false);
        productRepository.save(product);
    }

    public ProductDto getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
