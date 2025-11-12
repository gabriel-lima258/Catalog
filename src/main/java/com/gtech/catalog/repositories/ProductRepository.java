package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = """ 
           SELECT * FROM tb_product
           WHERE UPPER(name)
           LIKE UPPER(CONCAT('%', :name, '%'))
           """)
    Page<Product> searchByName(String name, Pageable pageable);
}
