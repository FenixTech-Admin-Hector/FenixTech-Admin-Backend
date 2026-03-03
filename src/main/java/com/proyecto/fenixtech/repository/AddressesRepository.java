package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Addresses;

public interface AddressesRepository extends JpaRepository<Addresses, Integer> {

    List<Addresses> findByUser_UserId(Integer id);
    @Query(value = "SELECT * FROM addresses WHERE " +
           "(:street IS NULL OR LOWER(street) LIKE LOWER(CONCAT('%', :street, '%'))) AND " +
           "(:city IS NULL OR LOWER(city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:region IS NULL OR LOWER(region) = LOWER(:region)) AND " +
           "(:country IS NULL OR LOWER(country) = LOWER(:country)) AND " +
           "(:zipCode IS NULL OR LOWER(zip_code) = LOWER(:zipCode))", 
           nativeQuery = true)
    List<Addresses> findByConditions(
            @Param("street") String street,
            @Param("city") String city,
            @Param("region") String region,
            @Param("country") String country,
            @Param("zipCode") String zipCode
    );
}