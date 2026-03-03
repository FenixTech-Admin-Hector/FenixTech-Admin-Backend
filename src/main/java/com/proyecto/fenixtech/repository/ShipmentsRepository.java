package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Shipments;

public interface ShipmentsRepository extends JpaRepository<Shipments, Integer> {
    List<Shipments> findByOrder_OrderId(Integer id);

    @Query(value = "SELECT * FROM shipments WHERE " +
            "(:shipping_street IS NULL OR LOWER(shipping_street) LIKE LOWER(CONCAT('%', :shipping_street, '%'))) AND " +
            "(:shipping_city IS NULL OR LOWER(shipping_city) LIKE LOWER(CONCAT('%', :shipping_city, '%'))) AND " +
            "(:shipping_zip_code IS NULL OR LOWER(shipping_zip_code) = LOWER(:shipping_zip_code)) AND " +
            "(:shipping_country IS NULL OR LOWER(shipping_country) = LOWER(:shipping_country)) AND " +
            "(:tracking_number IS NULL OR LOWER(tracking_number) = LOWER(:tracking_number)) AND " +
            "(:carrier_name IS NULL OR LOWER(carrier_name) = LOWER(:carrier_name)) AND " +
            "(:status IS NULL OR shipment_status = :status)", nativeQuery = true)
    List<Shipments> findByConditions(
            @Param("shipping_street") String shippingStreet,
            @Param("shipping_city") String shippingCity,
            @Param("shipping_zip_code") String shippingZipCode,
            @Param("shipping_country") String shippingCountry,
            @Param("tracking_number") String trackingNumber,
            @Param("carrier_name") String carrierName,
            @Param("status") String status);

}