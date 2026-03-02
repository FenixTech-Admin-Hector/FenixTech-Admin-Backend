package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.model.enums.Rol;


@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Users> findAllUsers(){
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Users findByUsersId(Integer id){
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + id));
    }

    @Transactional(readOnly = true)
    public Users findByEmail(String email){
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con email:" + email));
    }

    @Transactional(readOnly = true)
    public List<Users> findByRole(Rol rol) {
        return usersRepository.findByRole(rol);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtOrderByDesc() {
        return usersRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtOrderByAsc() {
        return usersRepository.findAllByOrderByCreatedAtAsc();
    }

    @Transactional(readOnly = true)
    //Se usa el mismo metodo para buscar por año y para buscar por rango de fechas
    public List<Users> findByCreatedAt(Integer year) {
        //Crea un LocalDateTime a partir de un año para revsar desde el 1 de enero a las 00:00:00 de ese año
        LocalDateTime inicioDelAno = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        //Crea un LocalDateTime a partir de un año para revisar hasta el 31 de diciembre a las 23:59:59 de ese año con los maximos milisegundos
        LocalDateTime finDelAno = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999999999);
        return usersRepository.findByCreatedAtBetween(inicioDelAno, finDelAno);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtBetween(LocalDate start, LocalDate end) {
        //Convierte el LocalDate a LocalDateTime con la fecha con hora 00:00:00
        LocalDateTime startDateTime = start.atStartOfDay();
        //Convierte el LocalDate en LocalDateTime con la fecha con hora 23:59:59 con los maximos milisegundos
        LocalDateTime endDateTime = end.atTime(java.time.LocalTime.MAX);
        return usersRepository.findByCreatedAtBetween(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return usersRepository.count();
    }



}
