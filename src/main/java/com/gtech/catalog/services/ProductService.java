package com.gtech.catalog.services;

import com.gtech.catalog.dto.CategoryDTO;
import com.gtech.catalog.dto.ProductDTO;
import com.gtech.catalog.entities.Category;
import com.gtech.catalog.entities.Product;
import com.gtech.catalog.projetions.ProductProjection;
import com.gtech.catalog.repositories.ProductRepository;
import com.gtech.catalog.services.exceptions.DatabaseException;
import com.gtech.catalog.services.exceptions.ResourceNotFoundException;
import com.gtech.catalog.utils.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found")
        );
        return new ProductDTO(entity);
    }

    // use case de listar produtos sem ou com pesquisa de filtros
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String categoryId, Pageable pageable) {
        // lista vazia ou array de ids
        List<Long> categoriesIds = Arrays.asList();
        if (!"0".equals(categoryId)) {
            categoriesIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();

        }
        // busca auxiliar para coletar id de produtos feitos na filtragem
        Page<ProductProjection> page = repository.searchProducts(name, categoriesIds, pageable);
        // capturando os id de produto feito na pesquisa auxiliar anterior para o DTO de produto
        List<Long> productsId = page.map(x -> x.getId()).toList();
        List<Product> entities = repository.searchProductsWithCategories(productsId);
        //noinspection unchecked
        entities = (List<Product>) Util.replace(page.getContent(), entities);

        List<ProductDTO> dtos = entities.stream().map(x -> new ProductDTO(x, x.getCategories())).toList();
        // transformando a lista em page
        Page<ProductDTO> pageDTO = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

        return pageDTO;
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            // usa referencia id e so abre o bd quando salva no repository
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation database");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        // limpar as categorias antes de inserir ou atualizar elas
        entity.getCategories().clear();

        for (CategoryDTO catDto: dto.getCategories()) {
            Category category = new Category();
            category.setId(catDto.getId()); // copia o valor do post dentro de uma nova categoria
            entity.getCategories().add(category); // adiciona o category no set dentro de produto
        }
    }
}
