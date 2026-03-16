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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartItemsService {
    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersService usersService;



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
        Users currentUser = usersService.getCurrentUser();
        
        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(userId)) {
            throw new AccessDeniedException("No tienes permiso para ver este carrito");
        }

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

    public Long countByCurrentUser() {
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cartItemsRepository.countByUser_UserId(currentUser.getUserId()); 
    }

    @Transactional
    public CartItems save(CartItemsRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        Integer productId = dto.getProductId();

        Products product = productsRepository.findByProductIdAndProductStatusActive(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto con ID " + productId + " no está disponible"));

        if (product.getCompany().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("No puedes añadir tus propios productos al carrito.");
        }

        return cartItemsRepository.findByUser_UserIdAndProduct_ProductId(currentUser.getUserId(), productId)
                .map(existing -> {
                    int newQuantity = existing.getQuantity() + dto.getQuantity();
                    if (newQuantity > product.getStock()) {
                        throw new IllegalArgumentException("Stock insuficiente. Máximo: " + product.getStock());
                    }
                    existing.setQuantity(newQuantity);
                    return cartItemsRepository.save(existing);
                })
                .orElseGet(() -> {
                    if (product.getStock() < dto.getQuantity()) {
                        throw new IllegalArgumentException("Stock insuficiente.");
                    }
                    CartItems newItem = new CartItems();
                    newItem.setUser(currentUser); 
                    newItem.setProduct(product);
                    newItem.setQuantity(dto.getQuantity());
                    return cartItemsRepository.save(newItem);
                });
    }

    @Transactional
    public void deleteById(Integer id) {
        CartItems item = cartItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") && 
            !item.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar este item");
        }

        cartItemsRepository.deleteById(item.getCartItemId());
    }

    @Transactional
    public CartItems update(Integer id, CartItemsRequestDTO dto) {
        CartItems cartUpdate = cartItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el item del carrito con ID: " + id));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") && 
            !cartUpdate.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para modificar este item");
        }
        
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
