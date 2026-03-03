package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Subcategories;

public interface SubcategoriesRepository extends JpaRepository<Subcategories, Integer>{
    List<Subcategories> findByCategory_CategoryId(Integer id);
    List<Subcategories> findByNameContainingIgnoreCase(String name);

}
