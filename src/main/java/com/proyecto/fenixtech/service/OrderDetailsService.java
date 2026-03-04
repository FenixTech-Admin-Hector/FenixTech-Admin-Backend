package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.OrderDetailsRepository;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.OrderDetails;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;


    @Transactional(readOnly = true)
    public List<OrderDetails> findAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrderDetails findById(Integer id) {
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de pedido no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<OrderDetails> findByOrderId(Integer orderId) {
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + orderId));
        
        return orderDetailsRepository.findByOrder_OrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderDetails> findByProductId(Integer productId) {
        productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        return orderDetailsRepository.findByProduct_ProductId(productId);
    }


    @Transactional(readOnly = true)
    public List<OrderDetails> findByPriceAndQuantityFilters(Double minPrice, Double maxPrice, Integer minQty, Integer maxQty) {
        return orderDetailsRepository.findByPriceAndQuantityFilters(minPrice, maxPrice, minQty, maxQty);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return orderDetailsRepository.count();
    }

}
