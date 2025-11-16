package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Product;
import com.gtech.catalog.entities.User;
import com.gtech.catalog.projetions.UserDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // consulta de users e roles para autenticação
    @Query(nativeQuery = true , value = """
    SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId , tb_role.authority
    FROM tb_user
    INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
    INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
    WHERE tb_user.email = :email
    """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

    // consulta N+1 ManyToMany para consultar o banco somente uma vez, assim reduzindo várias consultas desnecessárias
    @Query(value = """ 
           SELECT obj FROM User obj
           JOIN FETCH obj.roles
           WHERE UPPER(obj.firstName)
           LIKE UPPER(CONCAT('%', :firstName, '%'))
           """,
            countQuery = "SELECT COUNT(obj) FROM User obj JOIN obj.roles")
    Page<User> searchByName(String firstName, Pageable pageable);

    User findByEmail(String email);
}
