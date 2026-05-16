package com.proyecto.fenixtech.admin.category;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Categories;

public interface AdminCategoriesRepository extends JpaRepository<Categories, Integer> {
    Optional<Categories> findByNameIgnoreCase(String name);
}