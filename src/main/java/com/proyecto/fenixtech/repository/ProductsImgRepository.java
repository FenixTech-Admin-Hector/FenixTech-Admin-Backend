package com.proyecto.fenixtech.repository;
import com.proyecto.fenixtech.model.ProductsImg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductsImgRepository extends JpaRepository<ProductsImg, Integer>{
    List<ProductsImg> findByProduct_ProductId(Integer productId);
}
