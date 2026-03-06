package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Shipments;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ShipmentsRepository;

import java.util.Comparator;
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
    public List<Shipments> findByConditions(String street, String city, String zipCode, String country,
            String trackingNumber, String carrier, ShipmentStatus status) {
        String statusStr = (status != null) ? status.name().toLowerCase() : null;
        return shipmentsRepository.findByConditions(street, city, zipCode, country, trackingNumber, carrier, statusStr);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return shipmentsRepository.count();
    }

    @Transactional
    public Shipments save(Shipments shipment) {
        Orders order = ordersRepository.findById(shipment.getOrder().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (!order.getRequiresShipping()) {
            throw new IllegalArgumentException("Este pedido no requiere envío.");
        }

        Addresses latestAddress = order.getBuyer().getAddresses().stream()
            .max(Comparator.comparing(Addresses::getAddressId))
            .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene direcciones registradas."));
       

        shipment.setOrder(order);
        shipment.setShippingStreet(latestAddress.getStreet());
        shipment.setShippingCity(latestAddress.getCity());
        shipment.setShippingZipCode(latestAddress.getZipCode());
        shipment.setShippingCountry(latestAddress.getCountry());

        return shipmentsRepository.save(shipment);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!shipmentsRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el envío con id: " + id + " para eliminar");
        }
        shipmentsRepository.deleteById(id);
    }

    @Transactional
    public Shipments update(Integer id, Shipments shipment) {   
        Shipments shipmentUpdate = shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el envío con ID: " + id));

        shipmentUpdate.setTrackingNumber(shipment.getTrackingNumber());
        shipmentUpdate.setCarrier(shipment.getCarrier());
        shipmentUpdate.setStatus(shipment.getStatus());

        return shipmentsRepository.save(shipmentUpdate);
    }

}
