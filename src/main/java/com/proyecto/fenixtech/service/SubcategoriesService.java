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

}
