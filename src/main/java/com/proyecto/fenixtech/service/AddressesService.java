package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.AddressesRepository;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Users;
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
    public List<Addresses> findAllAddresses() {
        return addressesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Addresses findById(Integer id) {
        return addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Addresses> findByUserId(Integer id) {
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return addressesRepository.findByUser_UserId(id);

    }

    @Transactional(readOnly = true)
    public List<Addresses> findByConditions(String street, String city, String region, String country, String zipCode) {
        return addressesRepository.findByConditions(street, city, region, country, zipCode);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return addressesRepository.count();
    }

    @Transactional
    public Addresses save(Addresses address) {
        if (address.getUser() == null || address.getUser().getUserId() == null) {
            throw new IllegalArgumentException("La dirección debe estar asociada a un usuario válido con ID.");
        }

        Users user = usersRepository.findById(address.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario con ID " + address.getUser().getUserId() + " no existe"));

        // if (user.getRole() != Rol.PARTICULAR) {
        //     throw new IllegalArgumentException("EL rol del usuario tiene que ser: " + Rol.PARTICULAR.name());
        // }

        return addressesRepository.save(address);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!addressesRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la dirección con id: " + id + " para eliminar");
        }
        addressesRepository.deleteById(id);
    }

    @Transactional
    public Addresses update(Integer id, Addresses address) {
        Addresses addressUpdate = addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la dirección con ID: " + id));

        if (address.getUser() == null || address.getUser().getUserId() == null) {
            throw new IllegalArgumentException("La dirección debe incluir un usuario con ID.");
        }

        usersRepository.findById(address.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con id: " + address.getUser().getUserId()));

        addressUpdate.setStreet(address.getStreet());
        addressUpdate.setCity(address.getCity());
        addressUpdate.setRegion(address.getRegion());
        addressUpdate.setCountry(address.getCountry());
        addressUpdate.setZipCode(address.getZipCode());

        return addressesRepository.save(addressUpdate);
    }

}
