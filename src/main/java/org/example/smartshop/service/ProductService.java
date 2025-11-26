package org.example.smartshop.service;

import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.model.mapper.ProductMapper;
import org.example.smartshop.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

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
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
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
