package com.proyecto.fenixtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.engine.ReputationCalculator;
import com.proyecto.fenixtech.engine.BadgeRules;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
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

    @Autowired
    private ReputationCalculator reputationCalculator;

    @Autowired
    private BadgeRules badgeRules;

    private Integer calculatePointsForRating(Integer rating) {
        return switch (rating) {
            case 5 -> 15;
            case 4 -> 8;
            case 3 -> 3;
            case 2 -> -8;
            case 1 -> -15;
            case 0 -> -20;
            default -> 0;
        };
    }

    @Transactional
    public void proccessTransaction(Integer companyId, Products product, Integer quantity) {
        Companies company = companiesRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa con id: " + companyId + " no encontrada"));

        if (company.getImpactMetrics() == null) {
            company.setImpactMetrics(new ImpactMetrics(new EnvironmentalMetrics(), new SocialMetrics()));
        }

        ImpactMetrics metrics = company.getImpactMetrics();
        Double pointsToAdd = 0.0;

        String categoryName = product.getSubcategory().getCategory().getName();
        String subcategoryName = product.getSubcategory().getName();
        String condition = product.getStatus().name();

        ReputationCalculator.ItemMetrics itemMetrics = reputationCalculator.calculateMetrics(categoryName,
                subcategoryName, condition);

        Double calculatedPoints = itemMetrics.basePoints() * quantity;
        Double newEWaste = itemMetrics.eWasteKg() * quantity;
        Double newCo2 = itemMetrics.co2Kg() * quantity;

        if (product.getListingType() == ListingType.DONATION) {
            metrics.getSocial().setItemsDonated(metrics.getSocial().getItemsDonated() + quantity);
            pointsToAdd += calculatedPoints;
        } else if (product.getListingType() == ListingType.SALE) {
            metrics.getSocial().setItemsSoldDiscounted(metrics.getSocial().getItemsSoldDiscounted() + quantity);
            pointsToAdd += (calculatedPoints * 0.5);
        }

        Double oldTotalEWaste = metrics.getEnvironmental().getTotalEwasteSavedKg();
        metrics.getEnvironmental().setTotalEwasteSavedKg(oldTotalEWaste + newEWaste);
        metrics.getEnvironmental().setTotalCo2SavedKg(metrics.getEnvironmental().getTotalCo2SavedKg() + newCo2);

        Integer oldEwasteBlocks = (int) (oldTotalEWaste / 10);
        Integer newEwasteBlocks = (int) ((oldTotalEWaste + newEWaste) / 10);
        pointsToAdd += (newEwasteBlocks - oldEwasteBlocks) * 5;

        company.setImpactMetrics(metrics);
        Integer currentScore = company.getReputationScore() != null ? company.getReputationScore() : 0;
        company.setReputationScore(currentScore + pointsToAdd.intValue());

        companiesRepository.save(company);

        badgeRules.processBadges(company);
    }

    @Transactional
    public void processReviewScore(Integer companyId, Integer reviewScore) {
        Companies company = companiesRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa con id: " + companyId + " no encontrada"));

        Integer points = 0;

        points += calculatePointsForRating(reviewScore);

        Integer currentScore = company.getReputationScore() == null ? 0 : company.getReputationScore();
        Integer finalScore = currentScore + points;
        company.setReputationScore(finalScore < 0 ? 0 : finalScore);

        companiesRepository.save(company);

        badgeRules.processBadges(company);
    }

    @Transactional
    public void updateReviewScore(Integer companyId, Integer oldReviewScore, Integer newReviewScore){
        Companies company = companiesRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Empresa con id: " + companyId + " no encontrada"));

        Integer oldPoints = calculatePointsForRating(oldReviewScore);
        Integer newPoints = calculatePointsForRating(newReviewScore);

        Integer currentScore = company.getReputationScore() == null ? 0 : company.getReputationScore();
        Integer finalScore = currentScore - oldPoints + newPoints;
        company.setReputationScore(finalScore < 0 ? 0 : finalScore);

        companiesRepository.save(company);

        badgeRules.processBadges(company);
    }

    @Transactional
    public void deleteReviewScore(Integer companyId, Integer oldReviewScore){
        Companies company = companiesRepository.findById(companyId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe una empresa con id" + companyId));

        Integer oldPoints = calculatePointsForRating(oldReviewScore);
        Integer currentScore = company.getReputationScore() == null ? 0 : company.getReputationScore();
        Integer finalScore = currentScore - oldPoints;
        company.setReputationScore(finalScore < 0 ? 0 : finalScore);
        companiesRepository.save(company);
    }
}