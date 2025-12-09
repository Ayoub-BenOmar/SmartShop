package org.example.smartshop.service;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.model.entity.User;
import org.example.smartshop.model.mapper.ProductMapper;
import org.example.smartshop.repository.ProductRepository;
import org.example.smartshop.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (product.getIsVisible() == null) {
            product.setIsVisible(true);
        }
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
        product.setIsVisible(false);
        productRepository.save(product);
    }

    public ProductDto getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    public List<ProductDto> getAll() {
        return productRepository.findByIsVisibleTrue()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<ProductDto> getAllProductsPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByIsVisibleTrue(pageable);

        return productPage.map(productMapper::toDto);
    }
}
