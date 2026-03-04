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
    public List<Products> findAllProducts(){
        return productsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Products findById(Integer id){
        return productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Products> findBySubcategoryId(Integer id){
        subcategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con id: " + id));

        return productsRepository.findBySubcategory_SubcategoryId(id);    
    }

    @Transactional(readOnly = true)
    public List<Products> findByCompanyId(Integer id){
        companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));

        return productsRepository.findByCompany_CompanyId(id);
    }

    @Transactional(readOnly = true)
    public List<Products> findByProductTitle(String title){
        return productsRepository.findByProductTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Products> findByConditions(
            ProductStatus pStatus, ListingType lType, ConditionStatus cStatus,
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
        
        String pStatusStr = (pStatus != null) ? pStatus.name().toLowerCase() : null;
        String lTypeStr = (lType != null) ? lType.name().toLowerCase() : null;
        String cStatusStr = (cStatus != null) ? cStatus.name().toLowerCase() : null;

        return productsRepository.findByConditions(
                pStatusStr, lTypeStr, cStatusStr, minPrice, maxPrice, minStock, maxStock);
    }

    @Transactional(readOnly = true)
    public Long count(){
        return productsRepository.count();
    }






}
