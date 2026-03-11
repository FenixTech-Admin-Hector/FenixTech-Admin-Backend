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
    public Subcategories save(SubcategoriesRequestDTO dto) {
        // 1. Validamos que el ID de la categoría padre venga en el DTO
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("La subcategoría debe estar asociada a una categoría válida con ID.");
        }

        // 2. Verificamos que la categoría padre exista en la DB
        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID "
                        + dto.getCategoryId() + " no existe"));

        // 3. Opcional: Validar si ya existe una subcategoría con el mismo nombre
        subcategoriesRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Ya existe una subcategoría con el nombre: " + dto.getName());
                });

        // 4. Mapeamos el DTO a la Entidad
        Subcategories subcategory = new Subcategories();
        subcategory.setName(dto.getName());
        subcategory.setDescription(dto.getDescription());
        subcategory.setCategory(category);
        // subcategory.setIsActive(true);

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
