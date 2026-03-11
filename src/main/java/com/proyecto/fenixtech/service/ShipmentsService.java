package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.ShipmentRequestDTO;
import com.proyecto.fenixtech.dto.ShipmentResponseDTO;
import com.proyecto.fenixtech.dto.ShipmentUpdateCarrierDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Shipments;
import com.proyecto.fenixtech.model.ShippingCarriers;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ShipmentsRepository;
import com.proyecto.fenixtech.repository.ShippingCarriersRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(readOnly = true)
    public List<Shipments> findAllShipments() {
        return shipmentsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShipmentResponseDTO findById(Integer id) {
        Shipments shipment = shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con id: " + id));

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
    public Shipments save(ShipmentRequestDTO dto) {

        Orders order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (!order.getRequiresShipping()) {
            throw new IllegalArgumentException("Este pedido no requiere envío.");
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
    public Shipments update(Integer id, ShipmentUpdateCarrierDTO dto) {
        Shipments shipment = shipmentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado"));

        Orders order = shipment.getOrder();
        ShippingCarriers oldCarrier = shipment.getCarrier();
        ShippingCarriers newCarrier = shippingCarriersRepository.findById(dto.getCarrierId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe esa empresa de transportes"));

        Double totalSinEnvio = order.getTotalAmount() - oldCarrier.getBasePrice();
        order.setTotalAmount(totalSinEnvio + newCarrier.getBasePrice());
        ordersRepository.save(order);

        shipment.setCarrier(newCarrier);

        String prefix = newCarrier.getCarrierName().substring(0, 3).toUpperCase();
        String randomCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        shipment.setTrackingNumber(prefix + "-" + randomCode);

        return shipmentsRepository.save(shipment);
    }

}
