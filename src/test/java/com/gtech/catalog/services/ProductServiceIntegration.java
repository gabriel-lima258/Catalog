package com.gtech.catalog.services;

import com.gtech.catalog.dto.ProductDTO;
import com.gtech.catalog.repositories.ProductRepository;
import com.gtech.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // rollback no banco apos cada teste
public class ProductServiceIntegration {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);
        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void findAllPageShouldReturnPageWhenPageIsZeroAndSizeTen() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll("", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPageShouldReturnAnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageable = PageRequest.of(100, 10);
        Page<ProductDTO> result = service.findAll("", pageable);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPageShouldReturnSortedPageWhenSortByName() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAll("", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findPageProductShouldReturnPageProductWhenSearchByName() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll("Macbook Pro", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Macbook Pro", result.getContent().getFirst().getName());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }


}
