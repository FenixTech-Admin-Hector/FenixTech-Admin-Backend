package com.proyecto.fenixtech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer>{
    Optional<Categories> findByNameIgnoreCase(String name);
} 