package com.gtech.catalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtech.catalog.dto.CategoryDTO;
import com.gtech.catalog.utils.factory.CategoryFactoryTest;
import com.gtech.catalog.services.CategoryService;
import com.gtech.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// excluindo as dependencias de security nos testes unitários
@WebMvcTest(value = CategoryController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private CategoryDTO categoryDTO;
    private PageImpl<CategoryDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 5L;
        categoryDTO = CategoryFactoryTest.createCategoryDTO();
        page = new PageImpl<>(List.of(categoryDTO));

        // findAll
        Mockito.when(categoryService.findAll(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(page);
        // findById
        Mockito.when(categoryService.findById(existingId)).thenReturn(categoryDTO);
        Mockito.when(categoryService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        // update
        Mockito.when(categoryService.update(eq(existingId), any())).thenReturn(categoryDTO);
        Mockito.when(categoryService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        // delete
        Mockito.doNothing().when(categoryService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(categoryService).delete(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(categoryService).delete(dependentId);
        // insert
        Mockito.when(categoryService.insert(any())).thenReturn(categoryDTO);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/categories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnCategoryWhenIdExists() throws Exception {
        mockMvc.perform(get("/categories/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // jsonPath analisa o corpo da resposta
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/categories/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void insertShouldInsertNewCategoryAndReturnCategoryDTO() throws Exception {
        // usando object mapper para transformar objeto em json
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        mockMvc.perform(post("/categories")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void updateShouldUpdateCategoryWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        mockMvc.perform(put("/categories/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        mockMvc.perform(put("/categories/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists() throws Exception {
        mockMvc.perform(delete("/categories/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/categories/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
