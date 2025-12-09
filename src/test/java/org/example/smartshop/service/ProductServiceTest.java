package org.example.smartshop.service;

import org.example.smartshop.model.dto.ProductDto;
import org.example.smartshop.model.entity.Product;
import org.example.smartshop.model.mapper.ProductMapper;
import org.example.smartshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void createAndGetById() {
        ProductDto dto = new ProductDto(null, "Test", 10.0, 5, true);
        Product entity = new Product(null, "Test", 10.0, 5, true);
        Product saved = new Product(1, "Test", 10.0, 5, true);

        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(entity);
        when(productRepository.save(any(Product.class))).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(new ProductDto(1, "Test", 10.0, 5, true));
        when(productRepository.findById(1)).thenReturn(Optional.of(saved));

        ProductDto created = productService.create(dto);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1);

        ProductDto fetched = productService.getById(1);
        assertThat(fetched.getName()).isEqualTo("Test");

        verify(productMapper, times(1)).toEntity(dto);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void pagedReturnsCorrectContent() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            products.add(new Product(i, "P" + i, 5.0 + i, 10 + i, true));
        }

        Pageable firstPage = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(products.subList(0, 10), firstPage, products.size());

        when(productRepository.findByIsVisibleTrue(firstPage)).thenReturn(page);
        when(productMapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return new ProductDto(p.getId(), p.getName(), p.getUnitPrice(), p.getAvailableQuantity(), p.getIsVisible());
        });

        Page<ProductDto> result = productService.getAllProductsPaged(0, 10);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent()).hasSize(10);

        Pageable secondPage = PageRequest.of(1, 10);
        Page<Product> page2 = new PageImpl<>(products.subList(10, 15), secondPage, products.size());
        when(productRepository.findByIsVisibleTrue(secondPage)).thenReturn(page2);

        Page<ProductDto> result2 = productService.getAllProductsPaged(1, 10);
        assertThat(result2.getContent()).hasSize(5);
    }
}
