package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CartItemsRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.CartItems;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.Users;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemsService {
    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;


    @Transactional(readOnly = true)
    public List<CartItems> findAllCartItems() {
        return cartItemsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CartItems findById(Integer id) {
        return cartItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item del carrito no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByUserId(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        return cartItemsRepository.findByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByProductId(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        return cartItemsRepository.findByProduct_ProductId(productId);
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByQuantityGreaterThan(Integer quantity) {
        return cartItemsRepository.findByQuantityGreaterThan(quantity);
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByQuantityLessThan(Integer quantity) {
        return cartItemsRepository.findByQuantityLessThan(quantity);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return cartItemsRepository.count();
    }

}
