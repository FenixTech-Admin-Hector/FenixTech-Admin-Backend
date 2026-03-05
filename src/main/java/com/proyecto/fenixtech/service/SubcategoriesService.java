package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.SubcategoriesRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Subcategories;

@Service
public class SubcategoriesService {
    @Autowired
    private SubcategoriesRepository subcategoriesRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;


    @Transactional(readOnly = true)
    public List<Subcategories> findAllSubcategories() {
        return subcategoriesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Subcategories findById(Integer id) {
        return subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findByCategoryId(Integer id) {
        categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        return subcategoriesRepository.findByCategory_CategoryId(id);
    }

    @Transactional(readOnly = true)
    public List<Subcategories> findByName(String name) {
        return subcategoriesRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return subcategoriesRepository.count();
    }

    @Transactional
    public Subcategories save(Subcategories subcategory) {
        if (subcategory.getCategory() == null || subcategory.getCategory().getCategoryId() == null) {
            throw new IllegalArgumentException("La subcategoría debe estar asociada a una categoría válida con ID.");
        }

        categoriesRepository.findById(subcategory.getCategory().getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " 
                + subcategory.getCategory().getCategoryId() + " no existe"));
            
        return subcategoriesRepository.save(subcategory);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!subcategoriesRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la subcategoría con id: " + id + " para eliminar");
        }
        subcategoriesRepository.deleteById(id);
    }

    @Transactional
    public Subcategories update(Integer id, Subcategories subcategory) {
        Subcategories existingSubcategories = subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la subcategoría con ID: " + id));
        
        if (subcategory.getCategory() == null || subcategory.getCategory().getCategoryId() == null) {
            throw new IllegalArgumentException("La subcategoría debe tener una categoría asociada.");
        }
        
        if (!categoriesRepository.existsById(subcategory.getCategory().getCategoryId())) {
            throw new ResourceNotFoundException("La categoría con ID " + subcategory.getCategory().getCategoryId() + " no existe");
        }

        existingSubcategories.setName(subcategory.getName());
        existingSubcategories.setDescription(subcategory.getDescription());
        existingSubcategories.setCategory(subcategory.getCategory());
        
        return subcategoriesRepository.save(existingSubcategories);

    }
        





}
