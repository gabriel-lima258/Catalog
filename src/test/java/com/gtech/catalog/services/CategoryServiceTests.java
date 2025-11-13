package com.gtech.catalog.services;

import com.gtech.catalog.dto.CategoryDTO;
import com.gtech.catalog.entities.Category;
import com.gtech.catalog.factory.CategoryFactoryTest;
import com.gtech.catalog.repositories.CategoryRepository;
import com.gtech.catalog.services.exceptions.DatabaseException;
import com.gtech.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Category> page; // PageImpl implica o argumento Page no teste
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        category = CategoryFactoryTest.createCategory(); // crio o produto
        categoryDTO = CategoryFactoryTest.createCategoryDTO(); // crio o produto
        page = new PageImpl<>(List.of(category)); // instancio uma page de produtos

        // joga exception quando id for dependente
        Mockito.doThrow(DataIntegrityViolationException.class).when(categoryRepository).deleteById(dependentId);

        // Mockito simula as chamadas do repository
        Mockito.when(categoryRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(categoryRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(categoryRepository.existsById(dependentId)).thenReturn(true); // entidade associada ao category
        // Arguments matches diz qual arg inserido do método
        Mockito.when(categoryRepository.searchByName(ArgumentMatchers.anyString(),(Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(category);

        Mockito.when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(categoryRepository.getReferenceById(dependentId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() {
        CategoryDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(categoryRepository).findById(existingId);
    }

    @Test
    public void findAllPageShouldReturnPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CategoryDTO> result = service.findAll("Celulares", pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(categoryRepository).searchByName("Celulares", pageable);
    }

    @Test
    public void saveShouldSaveNewProduct() {
        CategoryDTO result = service.insert(categoryDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldUpdateProductWhenIdExists() {
        CategoryDTO result = service.update(existingId, categoryDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dependentId, categoryDTO);
        });
        Mockito.verify(categoryRepository).getReferenceById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        // mockito verifica se algum mock foi chamado e quantas vezes
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(existingId);
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
