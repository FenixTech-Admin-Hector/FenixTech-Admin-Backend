package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.SubcategoriesRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.fenixtech.dto.ProductRequestUpdateDTO;
import com.proyecto.fenixtech.dto.ProductsRequestPostDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.ProductsImg;
import com.proyecto.fenixtech.model.Subcategories;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private SubcategoriesRepository subcategoriesRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ImageService imageService;



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
            Double minPrice, Double maxPrice, Integer minStock, Integer maxStock, String location) {

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
                lTypeStr, cStatusStr, minPrice, maxPrice, minStock, maxStock, location);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return productsRepository.count();
    }

    @Transactional
    public Products save(ProductsRequestPostDTO dto) {
        Users currentUser = usersService.getCurrentUser();
        
        Companies comp = companiesRepository.findByUser_UserIdAndIsActiveTrue(currentUser.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("No tienes una empresa asociada para crear productos"));

        Products product = new Products();

        product.setProductTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setStreet(dto.getStreet());
        product.setZipCode(dto.getZipCode());
        product.setCity(dto.getCity());
        product.setRegion(dto.getRegion());
        product.setCountry(dto.getCountry());
        product.setStock(dto.getStockQuantity());
        product.setListingType(dto.getListingType());
        product.setDescription(dto.getDescription());

        Subcategories sub = subcategoriesRepository.findById(dto.getSubcategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subcategoría no encontrada con ID: " + dto.getSubcategoryId()));
        product.setSubcategory(sub);
        product.setCompany(comp);

        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            List<ProductsImg> images = dto.getImageUrls().stream().map(file -> {
                // 1. Guardar archivo físico y obtener nombre único
                String nombreImagen = imageService.guardarImagen(file);
                
                // 2. Crear entidad para la BD con la URL que servirá WebConfig
                ProductsImg img = new ProductsImg();
                img.setImageUrl("/fenixtech/uploads/" + nombreImagen);
                img.setProduct(product);
                return img;
            }).collect(Collectors.toList());
            
            product.setProductsImg(images);
        }

        return productsRepository.save(product);
    }

    @Transactional
    public void deleteById(Integer id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe el producto con id: " + id + " para eliminar"));
        
        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !product.getCompany().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes modificar un producto que no pertenece a tu empresa.");
        }
        
        productsRepository.deleteCartItemsByProductId(id);

        product.setProductStatus(ProductStatus.HIDDEN);
        productsRepository.save(product);
    }

    @Transactional
    public Products update(Integer id, ProductRequestUpdateDTO dto) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !product.getCompany().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes modificar un producto que no pertenece a tu empresa.");
        }

        product.setProductTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setStatus(dto.getConditionStatus());
        product.setListingType(dto.getListingType());
        product.setStreet(dto.getStreet());
        product.setCity(dto.getCity());
        product.setRegion(dto.getRegion());
        product.setZipCode(dto.getZipCode());
        product.setCountry(dto.getCountry());

        if (dto.getStock() <= 0) {
            product.setProductStatus(ProductStatus.SOLD_OUT);
        } else {
            product.setProductStatus(ProductStatus.ACTIVE);
        }

        Subcategories sub = subcategoriesRepository.findById(dto.getSubcategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no válida"));
        product.setSubcategory(sub);

        if (dto.getNewImages() != null && !dto.getNewImages().isEmpty()) {
            for (MultipartFile file : dto.getNewImages()) {
                String nombreImagen = imageService.guardarImagen(file);
                
                ProductsImg newImg = new ProductsImg();
                newImg.setImageUrl("/fenixtech/uploads/" + nombreImagen);
                newImg.setProduct(product);
                
                product.getProductsImg().add(newImg);
            }
        }

        return productsRepository.save(product);
    }

}
