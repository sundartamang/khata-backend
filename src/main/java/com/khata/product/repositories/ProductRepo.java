package com.khata.product.repositories;

import com.khata.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    // search product base on name
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
