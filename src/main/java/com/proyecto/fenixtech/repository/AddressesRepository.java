package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Addresses;

public interface AddressesRepository extends JpaRepository<Addresses, Integer> {

    List<Addresses> findByUser_UserId(Integer id);
    List<Addresses> findByCityIgnoringCase(String city);
    List<Addresses> findByRegionIgnoringCase(String region);
    List<Addresses> findByZipCode(String zipCode);
    List<Addresses> findByCountryIgnoringCase(String country);

}