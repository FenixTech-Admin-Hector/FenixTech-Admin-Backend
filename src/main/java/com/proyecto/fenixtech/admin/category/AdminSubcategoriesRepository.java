package com.proyecto.fenixtech.admin.category;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Subcategories;

public interface AdminSubcategoriesRepository extends JpaRepository<Subcategories, Integer> {
    List<Subcategories> findByCategory_CategoryId(Integer id);
    Optional<Subcategories> findByNameIgnoreCase(String name);
}