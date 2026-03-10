package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.SubcategoriesRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private SubcategoriesRepository subcategoriesRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Transactional(readOnly = true)
    public List<Products> findAllProducts() {
        return productsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Products findById(Integer id) {
        return productsRepository.findByProductIdAndProductStatusActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Products> findBySubcategoryId(Integer id) {
        subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id));

        return productsRepository.findByProductStatusActiveAndSubcategory_SubcategoryId(id);
    }

    @Transactional(readOnly = true)
    public List<Products> findByCompanyId(Integer id) {
        companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));

        return productsRepository.findByProductStatusActiveAndCompany_CompanyId(id);
    }

    @Transactional(readOnly = true)
    public List<Products> findByProductTitle(String title) {
        return productsRepository.findByProductStatusActiveAndProductTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Products> findByConditions(
            ListingType lType, ConditionStatus cStatus,
            Double minPrice, Double maxPrice, Integer minStock, Integer maxStock) {

        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser negativo.");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("El precio máximo no puede ser negativo.");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo.");
        }

        if (minStock != null && minStock < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
        if (maxStock != null && maxStock < 0) {
            throw new IllegalArgumentException("El stock máximo no puede ser negativo.");
        }
        if (minStock != null && maxStock != null && minStock > maxStock) {
            throw new IllegalArgumentException("El stock mínimo no puede ser mayor al stock máximo.");
        }

        String lTypeStr = (lType != null) ? lType.name() : null;
        String cStatusStr = (cStatus != null) ? cStatus.name() : null;

        return productsRepository.findByConditions(
                lTypeStr, cStatusStr, minPrice, maxPrice, minStock, maxStock);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return productsRepository.count();
    }

    @Transactional
    public Products save(Products product) {
        if (product.getProductsImg() != null && !product.getProductsImg().isEmpty()) {
            product.getProductsImg().forEach(img -> {
                img.setProduct(product);
            });
        }

        return productsRepository.save(product);
    }

    @Transactional
    public void deleteById(Integer id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe el producto con id: " + id + " para eliminar"));
        //Se limpian los carritos que tienen ese producto en especifico
                      
        productsRepository.deleteCartItemsByProductId(id);        
        // Soft Delete: Cambiamos el estado a HIDDEN
        product.setProductStatus(ProductStatus.HIDDEN);
        productsRepository.save(product);
    }

    @Transactional
    public Products update(Integer id, Products product) {
        Products productUpdate = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con ID: " + id));

        productUpdate.setProductTitle(product.getProductTitle());
        productUpdate.setDescription(product.getDescription());
        productUpdate.setStatus(product.getStatus());
        productUpdate.setListingType(product.getListingType());
        productUpdate.setPrice(product.getPrice());
        productUpdate.setStock(product.getStock());
        if (product.getCompany() != null) {
            productUpdate.setProductStatus(product.getProductStatus());
        } else {
            productUpdate.setProductStatus(ProductStatus.ACTIVE);
        }
        productUpdate.setSubcategory(product.getSubcategory());
        productUpdate.setCompany(product.getCompany());

        return productsRepository.save(productUpdate);
    }

}
