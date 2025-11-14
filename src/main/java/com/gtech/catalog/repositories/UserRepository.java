package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Product;
import com.gtech.catalog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = """ 
           SELECT * FROM tb_user
           WHERE UPPER(first_name)
           LIKE UPPER(CONCAT('%', :name, '%'))
           """)
    Page<User> searchByName(String name, Pageable pageable);
}
