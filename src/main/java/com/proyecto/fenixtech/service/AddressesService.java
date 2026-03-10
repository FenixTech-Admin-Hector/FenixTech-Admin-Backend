package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.AddressesRepository;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.dto.AddressDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;

import java.util.List;

import org.apache.coyote.BadRequestException;
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
    public Addresses save(AddressDTO dto) {
        List<Addresses> existingAddresses = addressesRepository.findByConditions(
                dto.getStreet(),
                dto.getCity(),
                dto.getRegion(),
                dto.getCountry(),
                dto.getZipCode());

        boolean alreadyHasIt = existingAddresses.stream()
                .anyMatch(a -> a.getUser().getUserId().equals(dto.getUserId()));

        if (alreadyHasIt) {
            throw new IllegalArgumentException("Ya tienes esta dirección registrada en tu perfil.");
        }

        Users user = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(dto.getUserId(), Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Addresses address = new Addresses();
        address.setUser(user);
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setRegion(dto.getRegion());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());

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
    public Addresses update(Integer id, AddressDTO dto) {
        Addresses addressUpdate = addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la dirección con ID: " + id));

        Users user = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(dto.getUserId(), Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o no autorizado"));

        List<Addresses> duplicates = addressesRepository.findByConditions(
                dto.getStreet(), dto.getCity(), dto.getRegion(),
                dto.getCountry(), dto.getZipCode());

        boolean isDuplicate = duplicates.stream()
                .anyMatch(a -> a.getUser().getUserId().equals(dto.getUserId()) && !a.getAddressId().equals(id));

        if (isDuplicate) {
            throw new IllegalArgumentException("Ya tienes otra dirección registrada con estos mismos datos.");
        }

        addressUpdate.setStreet(dto.getStreet());
        addressUpdate.setCity(dto.getCity());
        addressUpdate.setRegion(dto.getRegion());
        addressUpdate.setCountry(dto.getCountry());
        addressUpdate.setZipCode(dto.getZipCode());
        addressUpdate.setUser(user);
        return addressesRepository.save(addressUpdate);
    }

}
