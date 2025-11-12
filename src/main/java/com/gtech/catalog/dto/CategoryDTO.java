package com.gtech.catalog.dto;

import com.gtech.catalog.entities.Category;

import java.time.Instant;

public class CategoryDTO {

    private Long id;
    private String name;
    private Instant createdAt;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
        createdAt = entity.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
