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

    // 1. INYECTAMOS TU NUEVO CALCULADOR
    @Autowired
    private ReputationCalculator reputationCalculator;

    @Transactional
    public void proccessTransaction(Integer companyId, Products product, Integer quantity) {
        Companies company = companiesRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa con id: " + companyId + " no encontrada"));

        if (company.getImpactMetrics() == null) {
            company.setImpactMetrics(new ImpactMetrics(new EnvironmentalMetrics(), new SocialMetrics()));
        }

        ImpactMetrics metrics = company.getImpactMetrics();
        Double pointsToAdd = 0.0;

        // 1. Extraemos los datos del producto
        String categoryName = product.getSubcategory().getCategory().getName();
        String subcategoryName = product.getSubcategory().getName();
        
        // CORRECCIÓN: Usamos ConditionStatus para obtener "NEW", "USED_GOOD", etc.
        String condition = product.getStatus().name(); 

        // 2. Calculamos las métricas a través de tu componente
        ReputationCalculator.ItemMetrics itemMetrics = reputationCalculator.calculateMetrics(categoryName,
                subcategoryName, condition);

        // 3. Multiplicamos por la cantidad vendida/donada
        Double calculatedPoints = itemMetrics.basePoints() * quantity;
        Double newEWaste = itemMetrics.eWasteKg() * quantity;
        Double newCo2 = itemMetrics.co2Kg() * quantity;

        // 4. Lógica de Puntos Sociales (Venta vs Donación)
        if (product.getListingType() == ListingType.DONATION) {
            metrics.getSocial().setItemsDonated(metrics.getSocial().getItemsDonated() + quantity);
            pointsToAdd += calculatedPoints;
        } else if (product.getListingType() == ListingType.SALE) {
            metrics.getSocial().setItemsSoldDiscounted(metrics.getSocial().getItemsSoldDiscounted() + quantity);
            pointsToAdd += (calculatedPoints * 0.5);
        }

        // 5. Lógica Medioambiental (CORREGIDA: Ya no hay 0.0)
        Double oldTotalEWaste = metrics.getEnvironmental().getTotalEwasteSavedKg();
        metrics.getEnvironmental().setTotalEwasteSavedKg(oldTotalEWaste + newEWaste);
        metrics.getEnvironmental().setTotalCo2SavedKg(metrics.getEnvironmental().getTotalCo2SavedKg() + newCo2);

        // Bloques de E-waste (5 puntos extra por cada 10kg salvados)
        Integer oldEwasteBlocks = (int) (oldTotalEWaste / 10);
        Integer newEwasteBlocks = (int) ((oldTotalEWaste + newEWaste) / 10);
        pointsToAdd += (newEwasteBlocks - oldEwasteBlocks) * 5;

        // 6. Guardamos los cambios
        company.setImpactMetrics(metrics);
        Integer currentScore = company.getReputationScore() != null ? company.getReputationScore() : 0;
        company.setReputationScore(currentScore + pointsToAdd.intValue());

        companiesRepository.save(company);
    }

    @Transactional
    public void processReviewScore(Integer companyId, Integer reviewScore) {
        // Este método está perfecto, lo dejamos igual
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

        // Si la penalización es mayor que los puntos actuales, lo dejamos en 0 para
        // evitar puntuaciones negativas
        Integer currentScore = company.getReputationScore() == null ? 0 : company.getReputationScore();
        Integer finalScore = currentScore + points;
        company.setReputationScore(finalScore < 0 ? 0 : finalScore);

        companiesRepository.save(company);
    }
}