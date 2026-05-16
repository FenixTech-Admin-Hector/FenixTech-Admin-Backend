package com.proyecto.fenixtech.admin.product;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.ProductsImg;

public interface AdminProductsImgRepository extends JpaRepository<ProductsImg, Integer> {
    // Los métodos básicos (como deleteById) ya vienen heredados por JpaRepository
}