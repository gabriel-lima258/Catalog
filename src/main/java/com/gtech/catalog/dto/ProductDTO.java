package com.gtech.catalog.dto;

import com.gtech.catalog.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;
    @Size(min = 5, max = 60, message = "Campo deve ter entre 5 a 60 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;
    @Positive(message = "Informe um valor positivo")
    private Double price;
    @PastOrPresent(message = "A data do produto não pode ser futura")
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
