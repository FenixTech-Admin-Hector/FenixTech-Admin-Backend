package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Shipments;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ShipmentsRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentsService {
    @Autowired
    private ShipmentsRepository shipmentsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Transactional(readOnly = true)
    public List<Shipments> findAllShipments() {
        return shipmentsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Shipments findById(Integer id) {
        return shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Shipments> findByOrderId(Integer orderId) {
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + orderId));
        return shipmentsRepository.findByOrder_OrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<Shipments> findByConditions(String street, String city, String zipCode, String country, String trackingNumber, String carrier, ShipmentStatus status) {
        String statusStr = (status != null) ? status.name().toLowerCase() : null;
        return shipmentsRepository.findByConditions(street, city, zipCode, country, trackingNumber, carrier, statusStr);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return shipmentsRepository.count();
    }

}
