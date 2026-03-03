package com.proyecto.fenixtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.json.ImpactMetrics;
import com.proyecto.fenixtech.model.json.EnvironmentalMetrics;
import com.proyecto.fenixtech.model.json.SocialMetrics;

@Service
public class ReputationService {
    @Autowired
    private CompaniesRepository companiesRepository;

    @Transactional
    public void proccessTransaction(Integer companyId, Products product, Integer quantity){
        Companies company = companiesRepository.findById(companyId)
            .orElseThrow(()-> new IllegalArgumentException("Empresa con id: " + companyId + " no encontrada"));

        if(company.getImpactMetrics() == null){
            company.setImpactMetrics(new ImpactMetrics(new EnvironmentalMetrics(), new SocialMetrics()));
        }

        ImpactMetrics metrics = company.getImpactMetrics();
        Integer pointsToAdd = 0;
        
        if(product.getListingType() == ListingType.DONATION){
            metrics.getSocial().setItemsDonated(metrics.getSocial().getItemsDonated() + quantity);
            pointsToAdd += (50 * quantity);
        }else if(product.getListingType() == ListingType.SALE){
            metrics.getSocial().setItemsSoldDiscounted(metrics.getSocial().getItemsSoldDiscounted() + quantity);
            pointsToAdd += (10 * quantity);
        }

        // Para calacular el e-waste de categorías queda pendiente hasta que las subcategorías no estén implementadas
        // String category = product.getCategory().getName().toLowerCase();
        Double eWastePerItem = 0.0;
        Double co2PerItem = 0.0;

        
        Double newEWaste = eWastePerItem * quantity;
        Double oldTotalEWaste = metrics.getEnvironmental().getTotalEwasteSavedKg();
        metrics.getEnvironmental().setTotalEwasteSavedKg(oldTotalEWaste + newEWaste);
        metrics.getEnvironmental().setTotalCo2SavedKg(metrics.getEnvironmental().getTotalCo2SavedKg() + (co2PerItem * quantity));

        Integer oldEwasteBlocks = (int) (oldTotalEWaste / 10);
        Integer newEwasteBlocks = (int) ((oldTotalEWaste + newEWaste) / 10);
        pointsToAdd += (newEwasteBlocks - oldEwasteBlocks) * 5;

        company.setImpactMetrics(metrics);
        companiesRepository.save(company);
        
    }
    @Transactional
    public void processReviewScore(Integer companyId, Integer reviewScore){
        Companies company = companiesRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa con id: " + companyId + " no encontrada"));
        
        Integer points = 0;

        points += switch (reviewScore) {
            case 5 -> 15;
            case 4 -> 8;
            case 3 -> 3;
            case 2 -> -8;
            case 1 -> -15;
            case 0 -> -20;
            default -> 0; 
        };

        points = (points < 0) ? 0 : points;

        company.setReputationScore((company.getReputationScore() == null ? 0 : company.getReputationScore()) + points);
        companiesRepository.save(company);
    }

}
