package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Role;
import com.gtech.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);
}
