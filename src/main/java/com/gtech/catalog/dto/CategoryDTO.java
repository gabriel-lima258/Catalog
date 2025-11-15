package com.gtech.catalog.dto;

import com.gtech.catalog.entities.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
