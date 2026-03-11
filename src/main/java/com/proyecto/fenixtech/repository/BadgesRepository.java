package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Badges;
import java.util.Optional;

public interface BadgesRepository extends JpaRepository<Badges, Integer> {
    List<Badges> findByBadgeNameContainingIgnoreCaseAndIsActiveTrue(String name);
    List<Badges> findByIsActiveTrue();
    Optional<Badges> findByBadgeIdAndIsActiveTrue(Integer badgeId);
    List<Badges> findByBadgeNameContainingIgnoreCase(String name);
    Long countByIsActiveTrue();
    Optional<Badges> findByBadgeNameIgnoreCase(String badgeName);



    

} 