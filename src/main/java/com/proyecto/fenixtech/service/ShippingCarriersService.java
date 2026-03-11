package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.ShippingCarrierRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.ShippingCarriers;
import com.proyecto.fenixtech.repository.ShippingCarriersRepository;

@Service
public class ShippingCarriersService {
    @Autowired
    private ShippingCarriersRepository shippingCarriersRepository;

    @Transactional(readOnly = true)
    public List<ShippingCarriers> findAllShippingCarriers() {
        return shippingCarriersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShippingCarriers findById(Integer id) {
        return shippingCarriersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa de transporte no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public ShippingCarriers findByCarrierName(String carrierName) {
        return shippingCarriersRepository.findByCarrierNameIgnoreCase(carrierName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empresa de transporte no encontrada con nombre: " + carrierName));
    }

    @Transactional
    public ShippingCarriers save(ShippingCarrierRequestDTO dto) {
        if (!dto.getTrackingUrl().contains("{}")) {
            throw new IllegalArgumentException("La URL de seguimiento debe incluir '{}' como marcador de posición.");
        }

        ShippingCarriers carrier = new ShippingCarriers();
        carrier.setCarrierName(dto.getCarrierName());
        carrier.setCarrierLogo(dto.getCarrierLogo());
        carrier.setTrackingUrl(dto.getTrackingUrl());
        carrier.setBasePrice(dto.getBasePrice());
        carrier.setEstimatedDays(dto.getEstimatedDays());

        return shippingCarriersRepository.save(carrier);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!shippingCarriersRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la empresa de transporte con id: " + id + " para eliminar");
        }
        shippingCarriersRepository.deleteById(id);
    }

    @Transactional
    public ShippingCarriers update(Integer id, ShippingCarrierRequestDTO dto) {
        ShippingCarriers existingCarrier = shippingCarriersRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró la empresa de transporte con ID: " + id));

        if (dto.getTrackingUrl() != null && !dto.getTrackingUrl().contains("{}")) {
            throw new IllegalArgumentException(
                    "La URL de seguimiento debe contener '{}' para insertar el número de tracking.");
        }

        existingCarrier.setTrackingUrl(dto.getTrackingUrl());
        existingCarrier.setCarrierName(dto.getCarrierName());
        existingCarrier.setCarrierLogo(dto.getCarrierLogo());
        existingCarrier.setTrackingUrl(dto.getTrackingUrl());
        existingCarrier.setBasePrice(dto.getBasePrice());
        existingCarrier.setEstimatedDays(dto.getEstimatedDays());

        return shippingCarriersRepository.save(existingCarrier);
    }

    public String buildTrackingUrl(Integer carrierId, String trackingNumber) {
        ShippingCarriers carrier = findById(carrierId);
        if (carrier.getTrackingUrl() == null || trackingNumber == null) {
            return null;
        }

        return carrier.getTrackingUrl().replace("{}", trackingNumber);
    }

}
