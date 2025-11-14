package com.gtech.catalog.dto;

import com.gtech.catalog.entities.User;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    List<RoleDTO> roles = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
