package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.CompanyBadgeId;
import com.proyecto.fenixtech.model.CompanyBadges;
import java.time.LocalDateTime;


public interface CompanyBadgesRepository extends JpaRepository<CompanyBadges, CompanyBadgeId>{
    List<CompanyBadges> findByCompany_CompanyId(Integer id);
    List<CompanyBadges> findByAwardedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
