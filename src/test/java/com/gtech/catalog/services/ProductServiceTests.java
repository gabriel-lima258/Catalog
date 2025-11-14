package com.gtech.catalog.services;

import com.gtech.catalog.dto.ProductDTO;
import com.gtech.catalog.entities.Product;
import com.gtech.catalog.factory.ProductFactoryTest;
import com.gtech.catalog.repositories.CategoryRepository;
import com.gtech.catalog.repositories.ProductRepository;
import com.gtech.catalog.services.exceptions.DatabaseException;
import com.gtech.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

// teste de unidade
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    // injetando mocks para não precisar instanciar dependências
    @InjectMocks
    private ProductService service;

    // instanciando as dependencias de services ficticios
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page; // PageImpl implica o argumento Page no teste
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        product = ProductFactoryTest.createProduct(); // crio o produto
        productDTO = ProductFactoryTest.createProductDTO(); // crio o produto
        page = new PageImpl<>(List.of(product)); // instancio uma page de produtos

        // delete method
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        // Mockito simula as chamadas do repository
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true); // entidade associada ao product
        // Arguments matches diz qual arg inserido do método
        Mockito.when(productRepository.searchByName(ArgumentMatchers.anyString(),(Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productRepository.getReferenceById(dependentId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() {
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findAllPageShouldReturnPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll("Iphone", pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).searchByName("Iphone", pageable);
    }

    @Test
    public void saveShouldSaveNewProduct() {
        ProductDTO result = service.insert(productDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldUpdateProductWhenIdExists() {
        ProductDTO result = service.update(existingId, productDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dependentId, productDTO);
        });
        Mockito.verify(productRepository).getReferenceById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        // mockito verifica se algum mock foi chamado e quantas vezes
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }
}
