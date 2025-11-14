package com.gtech.catalog.dto;

import com.gtech.catalog.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserInsertDTO extends UserDTO {

    private String password;

    public UserInsertDTO() {
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public UserInsertDTO(User entity) {
        password = entity.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
