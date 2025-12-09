package org.example.smartshop.repository;

import org.example.smartshop.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByIsVisibleTrue(Pageable pageable);
    List<Product> findByIsVisibleTrue();

    Page<Product> findByNameContainingIgnoreCaseAndIsVisibleTrue(String name, Pageable pageable);
}
