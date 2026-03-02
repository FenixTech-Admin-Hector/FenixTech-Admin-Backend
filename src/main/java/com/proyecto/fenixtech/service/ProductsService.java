package com.proyecto.fenixtech.service;
import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.Companies;
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
    private CategoriesRepository categoriesRepository;

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
    public List<Products> findByProductStatus(ProductStatus productStatus){
        return productsRepository.findByProductStatus(productStatus);
    }

    @Transactional(readOnly = true)
    public List<Products> findByListingType(ListingType listingType){
        return productsRepository.findByListingType(listingType);
    }

    @Transactional(readOnly = true)
    public List<Products> findByStatus(ConditionStatus status){
        return productsRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<Products> findByMultipleFilters(ProductStatus pStatus, ListingType lType, ConditionStatus cStatus){
        return productsRepository.findByMultipleFilters(pStatus, lType, cStatus);
    }

    @Transactional(readOnly = true)
    public List<Products> findByCategoryId(Integer id){
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        return productsRepository.findByCategory_CategoryId(id);    
    }

    @Transactional(readOnly = true)
    public List<Products> findByCompanyId(Integer id){
        Companies company = companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));

        return productsRepository.findByCompany_CompanyId(id);
    }


    @Transactional(readOnly = true)
    public List<Products> findByPriceGreaterThan(Double price){
        return productsRepository.findByPriceGreaterThan(price);
    }

    @Transactional(readOnly = true)
    public List<Products> findByPriceLessThan(Double price){
        return productsRepository.findByPriceLessThan(price);
    }

    @Transactional(readOnly = true)
    public List<Products> findByStockGreaterThan(Integer stock){
        return productsRepository.findByStockGreaterThan(stock);
    }

    @Transactional(readOnly = true)
    public List<Products> findByWithoutStock(){
        return productsRepository.findByStockEquals(0);
    }

    @Transactional(readOnly = true)
    public List<Products> findByStockAvailable(){
        return productsRepository.findByStockGreaterThan(0);
    }   

    @Transactional(readOnly = true)
    public List<Products> findByProductTitle(String title){
        return productsRepository.findByProductTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public Long count(){
        return productsRepository.count();
    }






}
