package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.ProductsImgRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.ProductsImg;
import com.proyecto.fenixtech.model.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductsImgService {
    @Autowired
    private ProductsImgRepository productsImgRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersService usersService;



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
    public void deleteById(Integer id) {
        ProductsImg img = productsImgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

        Users currentUser = usersService.getCurrentUser();



        if (!currentUser.getRole().name().equals("ADMIN") &&
                !img.getProduct().getCompany().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes borrar imágenes de la competencia");
        }

        productsImgRepository.delete(img);
    }

}
