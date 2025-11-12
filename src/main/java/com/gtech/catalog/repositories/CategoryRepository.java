package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true, value = """ 
           SELECT * FROM tb_category
           WHERE UPPER(name)
           LIKE UPPER(CONCAT('%', :name, '%'))
           """)
    Page<Category> searchByName(String name, Pageable pageable);
}
