package com.proyecto.fenixtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.proyecto.fenixtech.model.enums.Rol;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
     * findAll()
     * findById(id)
     * 
     * count()
     * 
     * equals(User)
     * exist(User)
     * existById(id)
     */
    List<Users> findByRole(Rol role);
    Optional<Users> findByEmail(String email);
    List<Users> findAllByCreatedAtOrderByDesc();
    List<Users> findAllByCreatedAtOrderByAsc();
    List<Users> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);


}
