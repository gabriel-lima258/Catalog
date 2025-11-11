package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Product;
import com.gtech.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
