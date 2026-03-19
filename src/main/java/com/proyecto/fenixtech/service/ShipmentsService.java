package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.ShipmentRequestDTO;
import com.proyecto.fenixtech.dto.ShipmentResponseDTO;
import com.proyecto.fenixtech.dto.ShipmentUpdateStatusDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Shipments;
import com.proyecto.fenixtech.model.ShippingCarriers;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.OrderStatus;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ShipmentsRepository;
import com.proyecto.fenixtech.repository.ShippingCarriersRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentsService {
    @Autowired
    private ShipmentsRepository shipmentsRepository;

    @Autowired
    private ShippingCarriersRepository shippingCarriersRepository;

    @Autowired
    private ShippingCarriersService shippingCarriersService;

    @Autowired
    private OrdersRepository ordersRepository;
    
    @Autowired
    private UsersService usersService;


    @Transactional(readOnly = true)
    public List<Shipments> findAllShipments() {
        return shipmentsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShipmentResponseDTO findById(Integer id) {
        Shipments shipment = shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con id: " + id));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !shipment.getOrder().getBuyer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para ver los detalles de este envío.");
        }

        ShipmentResponseDTO dto = new ShipmentResponseDTO();
        dto.setShipmentId(shipment.getShipmentId());
        dto.setOrderId(shipment.getOrder().getOrderId());
        dto.setCarrierName(shipment.getCarrier().getCarrierName());
        dto.setCarrierLogo(shipment.getCarrier().getCarrierLogo());
        dto.setTrackingNumber(shipment.getTrackingNumber());
        dto.setStatus(shipment.getStatus());
        dto.setShippingStreet(shipment.getShippingStreet());
        dto.setShippingCity(shipment.getShippingCity());

        String fullUrl = shippingCarriersService.buildTrackingUrl(
                shipment.getCarrier().getCarrierId(),
                shipment.getTrackingNumber());
        dto.setTrackingUrl(fullUrl);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<Shipments> findByOrderId(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + orderId));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") &&
                !order.getBuyer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes ver los envíos de un pedido que no te pertenece.");
        }

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
    public Shipments save(ShipmentRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        Orders order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (!order.getBuyer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes crear un envío para un pedido que no es tuyo.");
        }

        ShippingCarriers carrier = shippingCarriersRepository.findById(dto.getCarrierId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa de transporte no encontrada"));

        Double nuevoTotal = order.getTotalAmount() + carrier.getBasePrice();
        order.setTotalAmount(nuevoTotal);
        ordersRepository.save(order);

        Shipments shipment = new Shipments();
        shipment.setOrder(order);
        shipment.setCarrier(carrier);

        String prefix = carrier.getCarrierName().substring(0, Math.min(carrier.getCarrierName().length(), 3))
                .toUpperCase();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        shipment.setTrackingNumber(prefix + "-" + randomSuffix);

        if (dto.getShippingStreet() != null && !dto.getShippingStreet().isBlank()) {
            shipment.setShippingStreet(dto.getShippingStreet());
            shipment.setShippingCity(dto.getShippingCity());
            shipment.setShippingZipCode(dto.getShippingZipCode());
            shipment.setShippingCountry(dto.getShippingCountry());
        } else {
            Addresses latestAddress = order.getBuyer().getAddresses().stream()
                    .max(Comparator.comparing(Addresses::getAddressId))
                    .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene direcciones registradas."));

            shipment.setShippingStreet(latestAddress.getStreet());
            shipment.setShippingCity(latestAddress.getCity());
            shipment.setShippingZipCode(latestAddress.getZipCode());
            shipment.setShippingCountry(latestAddress.getCountry());
        }

        shipment.setStatus(ShipmentStatus.PREPARING);

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
    public Shipments updateStatus(Integer id, ShipmentUpdateStatusDTO dto) {
        Shipments shipment = shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado"));

        shipment.setStatus(dto.getStatus());

        if (dto.getStatus() == ShipmentStatus.DELIVERED) {
            Orders order = shipment.getOrder();

            order.setStatus(OrderStatus.COMPLETED);

            ordersRepository.save(order);
        }

        return shipmentsRepository.save(shipment);
    }

}
