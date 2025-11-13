package com.gtech.catalog.factory;

import com.gtech.catalog.dto.CategoryDTO;
import com.gtech.catalog.entities.Category;

public class CategoryFactoryTest {

    public static Category createCategory() {
        return new Category(1L, "Celulares");
    }

    public static CategoryDTO createCategoryDTO() {
        Category category = createCategory();
        return new CategoryDTO(category);
    }
}
