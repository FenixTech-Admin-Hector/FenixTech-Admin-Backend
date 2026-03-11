package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CartItemsRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.CartItemsRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.CartItems;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.ProductStatus;

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
    public CartItems save(CartItemsRequestDTO dto) {
        Integer userId = dto.getUserId();
        Integer productId = dto.getProductId();

        Products product = productsRepository.findByProductIdAndProductStatusActive(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto con ID " + productId + " no está disponible o ha sido retirado"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario con ID " + userId + " no existe"));

        if (product.getCompany().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("No puedes añadir tus propios productos al carrito.");
        }

        return cartItemsRepository.findByUser_UserIdAndProduct_ProductId(userId, productId)
                .map(existing -> {
                    int newQuantity = existing.getQuantity() + dto.getQuantity();

                    if (newQuantity > product.getStock()) {
                        throw new IllegalArgumentException(
                                "No hay suficiente stock disponible. Stock actual: " + product.getStock());
                    }

                    existing.setQuantity(newQuantity);
                    return cartItemsRepository.save(existing);
                })
                .orElseGet(() -> {
                    if (product.getStock() < dto.getQuantity()) {
                        throw new IllegalArgumentException("Stock insuficiente para añadir este producto.");
                    }

                    CartItems newItem = new CartItems();
                    newItem.setUser(user);
                    newItem.setProduct(product);
                    newItem.setQuantity(dto.getQuantity());

                    return cartItemsRepository.save(newItem);
                });

    }

    @Transactional
    public void deleteById(Integer id) {
        if (!cartItemsRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el item del carrito con id: " + id + " para eliminar");
        }
        cartItemsRepository.deleteById(id);
    }

    @Transactional
    public CartItems update(Integer id, CartItemsRequestDTO dto) {
        CartItems cartUpdate = cartItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el item del carrito con ID: " + id));

        if (cartUpdate.getProduct().getProductStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException("Este producto ya no está disponible para la venta.");
        }

        if (dto.getQuantity() > cartUpdate.getProduct().getStock()) {
            throw new IllegalArgumentException(
                    "No hay suficiente stock disponible. Máximo actual: " + cartUpdate.getProduct().getStock());
        }

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
        }

        cartUpdate.setQuantity(dto.getQuantity());

        return cartItemsRepository.save(cartUpdate);
    }

}
