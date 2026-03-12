package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.ShippingCarriers;

public interface ShippingCarriersRepository extends JpaRepository<ShippingCarriers, Integer>{
    List<ShippingCarriers> findByIsActiveTrue();
    Optional<ShippingCarriers> findByCarrierIdAndIsActiveTrue(Integer id);
    Optional<ShippingCarriers> findByCarrierNameIgnoreCase(String name);
}
