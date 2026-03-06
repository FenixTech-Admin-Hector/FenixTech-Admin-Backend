package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CartItemsRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.CartItems;
import com.proyecto.fenixtech.model.Products;

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
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        return cartItemsRepository.findByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByProductId(Integer productId) {
        productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        return cartItemsRepository.findByProduct_ProductId(productId);
    }

    @Transactional(readOnly = true)
    public List<CartItems> findByQuantityFilters(Integer minQty, Integer maxQty) {
        if (minQty != null && minQty < 0) {
            throw new IllegalArgumentException("La cantidad mínima no puede ser negativa");
        }
        if (maxQty != null && maxQty < 0) {
            throw new IllegalArgumentException("La cantidad máxima no puede ser negativa");
        }
        if (minQty != null && maxQty != null && minQty > maxQty) {
            throw new IllegalArgumentException("La cantidad mínima no puede ser mayor a la cantidad máxima");
        }

        return cartItemsRepository.findByQuantityFilters(minQty, maxQty);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return cartItemsRepository.count();
    }

    @Transactional
    public CartItems save(CartItems cartItem) {
        if (cartItem.getUser() == null || cartItem.getUser().getUserId() == null) {
            throw new IllegalArgumentException("El item del carrito debe estar asociado a un usuario válido con ID.");
        }

        Integer userId = cartItem.getUser().getUserId();
        Integer productId = cartItem.getProduct().getProductId();

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + productId + " no existe"));

        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario con ID " + cartItem.getUser().getUserId() + " no existe"));

        List<CartItems> existingItems = cartItemsRepository.findByUser_UserId(userId);
        for (CartItems existing : existingItems) {
            if (existing.getProduct().getProductId().equals(productId)) {
                Integer newQuantity = existing.getQuantity() + cartItem.getQuantity();
                if (newQuantity > existing.getProduct().getStock()) {
                    throw new IllegalArgumentException("No hay suficiente stock para el producto");
                }

                existing.setQuantity(newQuantity);
                return cartItemsRepository.save(existing);
            }
        }

        if (product.getStock() < cartItem.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para añadir este producto.");
        }

        return cartItemsRepository.save(cartItem);

    }

    @Transactional
    public void deleteById(Integer id) {
        if (!cartItemsRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el item del carrito con id: " + id + " para eliminar");
        }
        cartItemsRepository.deleteById(id);
    }

    @Transactional
    public CartItems update(Integer id, Integer newQuantity) {
        CartItems cartUpdate = cartItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el item del carrito con ID: " + id));

        if (newQuantity < 1) {
            throw new IllegalArgumentException("La cantidad no puede ser menor a 1");
        }

        if (cartUpdate.getProduct().getStock() < newQuantity) {
            throw new IllegalArgumentException("La cantidad solicitada no puede ser mayor al stock disponible");
        }

        cartUpdate.setQuantity(newQuantity);

        return cartItemsRepository.save(cartUpdate);
    }

}
