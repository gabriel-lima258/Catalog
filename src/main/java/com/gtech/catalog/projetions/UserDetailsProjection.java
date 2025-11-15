package com.gtech.catalog.projetions;

// projeção de user e roles no banco de dados
public interface UserDetailsProjection {
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
