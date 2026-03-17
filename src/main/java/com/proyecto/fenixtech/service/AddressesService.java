package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.AddressesRepository;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.dto.AddressRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

@Service
public class AddressesService {
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;

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
        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para consultar las direcciones de este usuario");
        }

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
    public Addresses save(AddressRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        if (currentUser.getRole().name().equals("EMPRESA")) {
            List<Addresses> companyAddresses = addressesRepository.findByUser_UserId(currentUser.getUserId());
            if (!companyAddresses.isEmpty()) {
                throw new IllegalArgumentException("Una empresa solo puede tener una dirección registrada.");
            }
        }

        List<Addresses> existingAddresses = addressesRepository.findByConditions(
                dto.getStreet(), dto.getCity(), dto.getRegion(), dto.getCountry(), dto.getZipCode());

        boolean alreadyHasIt = existingAddresses.stream()
                .anyMatch(a -> a.getUser().getUserId().equals(currentUser.getUserId()));

        if (alreadyHasIt) {
            throw new IllegalArgumentException("Ya tienes esta dirección registrada en tu perfil.");
        }

        Addresses address = new Addresses();
        address.setUser(currentUser);
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setRegion(dto.getRegion());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());

        return addressesRepository.save(address);
    }

    @Transactional
    public void deleteById(Integer id) {
        Addresses address = addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada"));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !address.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta dirección");
        }

        addressesRepository.delete(address);
    }

    @Transactional
    public Addresses update(Integer id, AddressRequestDTO dto) {
        Addresses addressUpdate = addressesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la dirección con ID: " + id));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !addressUpdate.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para modificar esta dirección");
        }

        List<Addresses> duplicates = addressesRepository.findByConditions(
                dto.getStreet(), dto.getCity(), dto.getRegion(),
                dto.getCountry(), dto.getZipCode());

        boolean isDuplicate = duplicates.stream()
                .anyMatch(a -> a.getUser().getUserId().equals(currentUser.getUserId()) && !a.getAddressId().equals(id));

        if (isDuplicate) {
            throw new IllegalArgumentException("Ya tienes otra dirección registrada con estos mismos datos.");
        }

        addressUpdate.setStreet(dto.getStreet());
        addressUpdate.setCity(dto.getCity());
        addressUpdate.setRegion(dto.getRegion());
        addressUpdate.setCountry(dto.getCountry());
        addressUpdate.setZipCode(dto.getZipCode());

        return addressesRepository.save(addressUpdate);
    }

}
