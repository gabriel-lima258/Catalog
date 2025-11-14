package com.gtech.catalog.dto;

import com.gtech.catalog.entities.Role;
import com.gtech.catalog.entities.User;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {

    private Long id;
    private String authority;


    public RoleDTO() {
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
