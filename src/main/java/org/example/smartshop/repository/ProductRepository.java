package org.example.smartshop.repository;

import org.example.smartshop.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByIsVisibleTrue(Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndIsVisibleTrue(String name, Pageable pageable);
}
