package com.proyecto.fenixtech.engine;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.CompanyBadges;
import com.proyecto.fenixtech.model.CompanyBadgeId;
import com.proyecto.fenixtech.service.BadgesService;
import com.proyecto.fenixtech.repository.CompanyBadgesRepository;

@Component
public class BadgeRules{
    @Autowired
    private BadgesService badgesService;

    @Autowired
    private CompanyBadgesRepository companyBadgesRepository;

    /**
     * Método privado qpara buscar la insignia y asignarla.
     */
    private void assignBadgeIfEligible(Companies company, String badgeName) {
        
        // 1. Buscamos si la insignia existe y está ACTIVA
        badgesService.findByBadgeNameTrue(badgeName).stream() 
            .findFirst()
            .ifPresent(badge -> {
                
                // 2. Comprobamos si la empresa YA tiene esta insignia para no duplicarla
                Boolean alreadyHasBadge = companyBadgesRepository.existsById_CompanyIdAndId_BadgeId(
                        company.getCompanyId(), badge.getBadgeId() 
                );

                if (!alreadyHasBadge) {
                    // 3. Le asignamos la medalla y creamos la relación
                    CompanyBadges newBadgeAward = new CompanyBadges();
                    
                    // asignamos la clave compuesta (ID de Empresa + ID de Insignia)
                    CompanyBadgeId id = new CompanyBadgeId(company.getCompanyId(), badge.getBadgeId());
                    newBadgeAward.setId(id);
                    
                    newBadgeAward.setCompany(company);
                    newBadgeAward.setBadge(badge);
                    newBadgeAward.setAwardedAt(LocalDateTime.now()); // Guardamos cuándo la ganó

                    // 4. Guardamos en la tabla company_badges
                    companyBadgesRepository.save(newBadgeAward);
                }
            });
    }

    public void processBadges(Companies company){
        if (company.getImpactMetrics() == null) return;

        // 1. Regla 'Eco-Friendly': Más de 50kg de CO2 ahorrado
        if (company.getImpactMetrics().getEnvironmental() != null &&
            company.getImpactMetrics().getEnvironmental().getTotalCo2SavedKg() >= 50.0) {
            assignBadgeIfEligible(company, "Eco-Friendly");
        }

        // 2. Regla 'Community Star': 10 o más donaciones
        if (company.getImpactMetrics().getSocial() != null &&
            company.getImpactMetrics().getSocial().getItemsDonated() >= 10) {
            assignBadgeIfEligible(company, "Community Star");
        }

        // 3. Regla 'Top Seller': Más de 500 puntos de reputación
        if (company.getReputationScore() != null && 
            company.getReputationScore() >= 500) {
            assignBadgeIfEligible(company, "Top Seller");
        }
    }


}