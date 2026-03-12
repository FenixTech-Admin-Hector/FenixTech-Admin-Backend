package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Subcategories;

public interface SubcategoriesRepository extends JpaRepository<Subcategories, Integer> {
    List<Subcategories> findByCategory_CategoryId(Integer id);

    List<Subcategories> findByNameContainingIgnoreCase(String name);

    Optional<Subcategories> findByNameIgnoreCase(String name);

    List<Subcategories> findByIsActiveTrue();

    Optional<Subcategories> findBySubcategoryIdAndIsActiveTrue(Integer id);

    List<Subcategories> findByCategory_CategoryIdAndIsActiveTrue(Integer categoryId);

    List<Subcategories> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    Long countByIsActiveTrue();

}
