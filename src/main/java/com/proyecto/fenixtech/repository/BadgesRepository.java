package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Badges;

public interface BadgesRepository extends JpaRepository<Badges, Integer> {
    List<Badges> findByBadgeNameContainingIgnoreCase(String name);
    
    
} 