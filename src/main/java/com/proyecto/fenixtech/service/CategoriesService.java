package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.dto.CategoriesRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.enums.ProductStatus;
import com.proyecto.fenixtech.repository.CategoriesRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    public List<Categories> findAllCategories() {
        return categoriesRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Categories findById(Integer id) {
        return categoriesRepository.findByCategoryIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id:" + id));
    }

    @Transactional(readOnly = true)
    public Categories findByCategoryName(String name) {
        return categoriesRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nombre:" + name));
    }

    @Transactional(readOnly = true)
    public Long count() {
        return categoriesRepository.countByIsActiveTrue();
    }

    @Transactional
    public Categories save(CategoriesRequestDTO dto) {
        categoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(category -> {
                    throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
                });

        Categories category = new Categories();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return categoriesRepository.save(category);
    }

    @Transactional
    public void deleteById(Integer id) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        category.setIsActive(false);

        if (category.getSubcategories() != null) {
            category.getSubcategories().forEach(sub -> {
                sub.setIsActive(false);

                if (sub.getProducts() != null) {
                    sub.getProducts().forEach(p -> p.setProductStatus(ProductStatus.HIDDEN));
                }
            });
        }

        categoriesRepository.save(category);
    }

    @Transactional
    public Categories update(Integer id, CategoriesRequestDTO dto) {
        Categories existingCategory = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + id));

        categoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(existing -> {
                    if (!existing.getCategoryId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + dto.getName());
                    }
                });

        existingCategory.setName(dto.getName());
        existingCategory.setDescription(dto.getDescription());

        return categoriesRepository.save(existingCategory);
    }

}
