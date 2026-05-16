package com.proyecto.fenixtech.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Subcategories;
import com.proyecto.fenixtech.model.enums.ProductStatus;

@Service
public class AdminCategoriesService {

    @Autowired
    private AdminCategoriesRepository adminCategoriesRepository;

    @Autowired
    private AdminSubcategoriesRepository adminSubcategoriesRepository;

    // --- LÓGICA DE CATEGORÍAS ---

    @Transactional(readOnly = true)
    public List<Categories> findAllCategories() {
        return adminCategoriesRepository.findAll();
    }

    @Transactional
    public Categories saveCategory(AdminCategoriesRequestDTO dto) {
        adminCategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(category -> {
                    throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
                });

        Categories category = new Categories();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIsActive(true);

        return adminCategoriesRepository.save(category);
    }

    @Transactional
    public Categories updateCategory(Integer id, AdminCategoriesRequestDTO dto) {
        Categories existingCategory = adminCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + id));

        adminCategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(existing -> {
                    if (!existing.getCategoryId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + dto.getName());
                    }
                });

        existingCategory.setName(dto.getName());
        existingCategory.setDescription(dto.getDescription());

        return adminCategoriesRepository.save(existingCategory);
    }

    @Transactional
    public void toggleCategory(Integer id) {
        Categories category = adminCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        boolean newState = !category.getIsActive();
        category.setIsActive(newState);

        if (!newState && category.getSubcategories() != null) {
            category.getSubcategories().forEach(sub -> {
                sub.setIsActive(false);
                if (sub.getProducts() != null) {
                    sub.getProducts().forEach(p -> p.setProductStatus(ProductStatus.HIDDEN));
                }
            });
        }

        adminCategoriesRepository.save(category);
    }

    // --- LÓGICA DE SUBCATEGORÍAS ---

    @Transactional(readOnly = true)
    public List<Subcategories> findAllSubcategories() {
        return adminSubcategoriesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findSubcategoriesByParent(Integer categoryId) {
        return adminSubcategoriesRepository.findByCategory_CategoryId(categoryId);
    }

    @Transactional
    public Subcategories saveSubcategory(AdminSubcategoriesRequestDTO dto) {
        Categories category = adminCategoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " + dto.getCategoryId() + " no existe"));

        adminSubcategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya existe una subcategoría con el nombre: " + dto.getName());
                });

        Subcategories subcategory = new Subcategories();
        subcategory.setName(dto.getName());
        subcategory.setDescription(dto.getDescription());
        subcategory.setCategory(category);
        subcategory.setIsActive(true);

        return adminSubcategoriesRepository.save(subcategory);
    }

    @Transactional
    public Subcategories updateSubcategory(Integer id, AdminSubcategoriesRequestDTO dto) {
        Subcategories existingSub = adminSubcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la subcategoría con ID: " + id));

        Categories newCategory = adminCategoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " + dto.getCategoryId() + " no existe"));
        
        existingSub.setCategory(newCategory);

        adminSubcategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(sub -> {
                    if (!sub.getSubcategoryId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe otra subcategoría con el nombre: " + dto.getName());
                    }
                });

        existingSub.setName(dto.getName());
        existingSub.setDescription(dto.getDescription());

        return adminSubcategoriesRepository.save(existingSub);
    }

    @Transactional
    public void toggleSubcategory(Integer id) {
        Subcategories subcategory = adminSubcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id));

        boolean newState = !subcategory.getIsActive();
        subcategory.setIsActive(newState);

        if (!newState && subcategory.getProducts() != null) {
            subcategory.getProducts().forEach(product -> {
                product.setProductStatus(ProductStatus.HIDDEN);
            });
        }

        adminSubcategoriesRepository.save(subcategory);
    }
}