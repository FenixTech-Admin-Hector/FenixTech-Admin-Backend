package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Users;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.model.enums.Rol;


@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public List<Users> findAllUsers(){
        return usersRepository.findAll();
    }

    public Users findByUsersId(Integer id){
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + id));
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con email:" + email));
    }
    public List<Users> findByRole(Rol rol) {
        return usersRepository.findByRole(rol);
    }

    public List<Users> findByCreatedAtOrderByDesc() {
        return usersRepository.findByCreatedAtOrderByDesc();
    }

    public List<Users> findByCreatedAtOrderByAsc() {
        return usersRepository.findByCreatedAtOrderByAsc();
    }

    public List<Users> findByCreatedAt(LocalDateTime createdAt) {
        return usersRepository.findByCreatedAt(createdAt);
    }

    public List<Users> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return usersRepository.findByCreatedAtBetween(start, end);
    }


}
