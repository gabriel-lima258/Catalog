package com.gtech.catalog.repositories;

import com.gtech.catalog.entities.Product;
import com.gtech.catalog.projetions.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // consulta N+1 ManyToMany para consultar o banco somente uma vez, assim reduzindo várias consultas desnecessárias
    // consulta de products que possuem id de categorias e nome
    @Query(nativeQuery = true, value = """ 
            SELECT * FROM(
            SELECT DISTINCT tb_product.id, tb_product.name
            FROM tb_product
            INNER JOIN tb_product_category ON  tb_product.id = tb_product_category.product_id
            WHERE (:categoriesId IS NULL OR tb_product_category.category_id IN :categoriesId)
            AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
            ) AS tb_result
           """,
            countQuery = """ 
            SELECT COUNT(*) FROM(
            SELECT DISTINCT tb_product.id, tb_product.name
            FROM tb_product
            INNER JOIN tb_product_category ON  tb_product.id = tb_product_category.product_id
            WHERE (:categoriesId IS NULL OR tb_product_category.category_id IN :categoriesId)
            AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
            ) AS tb_result
           """)
    Page<ProductProjection> searchProducts(String name, List<Long> categoriesId, Pageable pageable);

    // consulta auxiliar de após pesquisa de produtos com categories id
    @Query(value = """
            SELECT obj FROM Product obj JOIN FETCH obj.categories
            WHERE obj.id IN :productsId
            """)
    List<Product> searchProductsWithCategories(List<Long> productsId);
}
