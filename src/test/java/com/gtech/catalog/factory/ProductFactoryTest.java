package com.gtech.catalog.factory;

import com.gtech.catalog.dto.ProductDTO;
import com.gtech.catalog.entities.Category;
import com.gtech.catalog.entities.Product;

import java.time.Instant;

public class ProductFactoryTest {

    public static Product createProduct() {
        Product product = new Product(1L, "Iphone", 12000.0, "Iphone 17", "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
        product.getCategories().add(new Category(1L, "Celulares"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
