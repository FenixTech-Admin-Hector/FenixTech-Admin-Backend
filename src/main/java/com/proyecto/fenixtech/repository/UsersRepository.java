package com.proyecto.fenixtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmailAndIsActiveTrue(String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUserIdAndIsActiveTrue(Integer id);

    @Query(value = "SELECT * FROM users u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:active IS NULL OR u.is_active = :active) AND " +
            "(:start IS NULL OR u.created_at >= :start) AND " +
            "(:end IS NULL OR u.created_at <= :end)", nativeQuery = true)
    List<Users> findUsersByFiltersNative(
            @Param("role") String role, 
            @Param("active") Boolean active,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
