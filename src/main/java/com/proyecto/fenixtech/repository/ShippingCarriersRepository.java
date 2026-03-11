package com.proyecto.fenixtech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.ShippingCarriers;

public interface ShippingCarriersRepository extends JpaRepository<ShippingCarriers, Integer>{
    Optional<ShippingCarriers> findByCarrierNameIgnoreCase(String carrierName);
}
