package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer>{
    Optional<Categories> findByNameIgnoreCase(String name);
    List<Categories> findByIsActiveTrue();
    Optional<Categories> findByCategoryIdAndIsActiveTrue(Integer id);
    Long countByIsActiveTrue();



    
} 