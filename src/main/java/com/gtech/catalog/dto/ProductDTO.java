package com.gtech.catalog.dto;

import com.gtech.catalog.entities.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;
    private String name;
    private Double price;
    private Instant date;
    private String description;
    private String imgUrl;

    List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price, Instant date, String description, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        price = entity.getPrice();
        date = entity.getDate();
        description = entity.getDescription();
        imgUrl = entity.getImgUrl();
        entity.getCategories().forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Instant getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
