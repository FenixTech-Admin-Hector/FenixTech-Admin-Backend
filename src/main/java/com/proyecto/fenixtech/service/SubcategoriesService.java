package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.SubcategoriesRepository;
import com.proyecto.fenixtech.dto.SubcategoriesRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Subcategories;
import com.proyecto.fenixtech.model.enums.ProductStatus;

@Service
public class SubcategoriesService {
    @Autowired
    private SubcategoriesRepository subcategoriesRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    public List<Subcategories> findAll() {
        return subcategoriesRepository.findAll(); 
    }

    @Transactional(readOnly = true)
    public Subcategories findById(Integer id) {
        return subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id)); //
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findAllSubcategories() {
        return subcategoriesRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Subcategories findByIdActive(Integer id) {
        return subcategoriesRepository.findBySubcategoryIdAndIsActiveTrue(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subcategoría no encontrada o inactiva con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findByCategoryId(Integer id) {
        categoriesRepository.findByCategoryIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva con id: " + id));

        return subcategoriesRepository.findByCategory_CategoryIdAndIsActiveTrue(id);
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findByName(String name) {
        return subcategoriesRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return subcategoriesRepository.countByIsActiveTrue();
    }

    @Transactional
    public Subcategories save(SubcategoriesRequestDTO dto) {
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("La subcategoría debe estar asociada a una categoría válida con ID.");
        }

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID "
                        + dto.getCategoryId() + " no existe"));

        subcategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya existe una subcategoría con el nombre: " + dto.getName());
                });

        Subcategories subcategory = new Subcategories();
        subcategory.setName(dto.getName());
        subcategory.setDescription(dto.getDescription());
        subcategory.setCategory(category);
        subcategory.setIsActive(true);

        return subcategoriesRepository.save(subcategory);
    }

    @Transactional
    public void deleteById(Integer id) {
        Subcategories subcategory = subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        subcategory.setIsActive(false);

        if (subcategory.getProducts() != null) {
            subcategory.getProducts().forEach(product -> {
                product.setProductStatus(ProductStatus.HIDDEN);
            });
        }

        subcategoriesRepository.save(subcategory);
    }

    @Transactional
    public Subcategories update(Integer id, SubcategoriesRequestDTO dto) {
        Subcategories existingSub = subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la subcategoría con ID: " + id));

        if (dto.getCategoryId() != null) {
            Categories newCategory = categoriesRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "La categoría con ID " + dto.getCategoryId() + " no existe"));
            existingSub.setCategory(newCategory);
        }

        subcategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(sub -> {
                    if (!sub.getSubcategoryId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ya existe otra subcategoría con el nombre: " + dto.getName());
                    }
                });

        existingSub.setName(dto.getName());
        existingSub.setDescription(dto.getDescription());

        return subcategoriesRepository.save(existingSub);
    }

}
