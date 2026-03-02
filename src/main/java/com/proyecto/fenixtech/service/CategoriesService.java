package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.repository.CategoriesRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    public List<Categories> findAllCategories() {
        return categoriesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categories findById(Integer id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id:" + id));
    }

    @Transactional(readOnly = true)
    public Categories findByCategoryName(String name) {
        return categoriesRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nombre:" + name));
    }

    @Transactional(readOnly = true)
    public Long count() {
        return categoriesRepository.count();
    }

}
