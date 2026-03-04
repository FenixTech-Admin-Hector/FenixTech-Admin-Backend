package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.AddressesRepository;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressesService {
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Addresses> findAllAddresses(){
        return addressesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Addresses findById(Integer id){
        return addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Addresses> findByUserId(Integer id){
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return  addressesRepository.findByUser_UserId(id);

    }

    @Transactional(readOnly = true)
    public List<Addresses> findByConditions(String street, String city, String region, String country, String zipCode){
        return addressesRepository.findByConditions(street, city, region, country, zipCode );
    }


    @Transactional(readOnly = true)
    public Long count(){
        return addressesRepository.count();
    }

}
