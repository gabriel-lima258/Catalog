package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Category;
import com.gtech.catalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
