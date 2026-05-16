package com.proyecto.fenixtech.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Importamos el Modelo compartido del grupo
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdminUsersRepository extends JpaRepository<Users, Integer> {

    // Buscar para desbanear o editar (encuentra a todos)
    Optional<Users> findById(Integer id);

    // Buscar solo a los activos (para el baneo)
    Optional<Users> findByUserIdAndIsActiveTrue(Integer id);

    // Comprobar si un email ya existe al editar
    Optional<Users> findByEmail(String email);

    // 🚀 LA MAGIA DEL BUSCADOR DEL ADMIN
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

    long countByRole(Rol role);
}