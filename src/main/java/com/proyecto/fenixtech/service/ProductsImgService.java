package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.ProductsImgRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.ProductsImg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductsImgService {
    @Autowired
    private ProductsImgRepository productsImgRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<ProductsImg> findAllProductsImg() {
        return productsImgRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductsImg findById(Integer id) {
        return productsImgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen de producto no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductsImg> findByProductId(Integer productId) {
        productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        return productsImgRepository.findByProduct_ProductId(productId);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return productsImgRepository.count();
    }

    @Transactional
    public ProductsImg save(ProductsImg productsImg) {
        if (productsImg.getProduct() == null || productsImg.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("La imagen debe estar asociada a un producto válido con ID.");
        }

        productsRepository.findById(productsImg.getProduct().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto con ID " + productsImg.getProduct().getProductId() + " no existe"));

        return productsImgRepository.save(productsImg);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!productsImgRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la imagen con id: " + id + " para eliminar");
        }
        productsImgRepository.deleteById(id);
    }

    @Transactional
    public ProductsImg update(Integer id, ProductsImg productsImg) {
        if (productsImg.getProduct() == null || productsImg.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("La imagen debe estar asociada a un producto válido con ID.");
        }

        ProductsImg existingImg = productsImgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la imagen con ID: " + id));

        existingImg.setImageUrl(productsImg.getImageUrl());

        return productsImgRepository.save(existingImg);
    }
}
