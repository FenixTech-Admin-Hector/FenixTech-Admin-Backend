package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Companies;

public interface CompaniesRepository extends JpaRepository<Companies, Integer> {

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
        List<Companies> findByIsActiveTrue();
        Optional<Companies> findByCompanyIdAndIsActiveTrue(Integer id);
        Optional<Companies> findByUser_UserIdAndIsActiveTrue(Integer id);
        List<Companies> findByCompanyNameContainingIgnoreCaseAndIsActiveTrue(String name);
        List<Companies> findTop3ByIsActiveTrueOrderByReputationScoreDesc();



        Long countByIsActiveTrue();
        

        @Query(value = "SELECT * FROM companies WHERE " +
                        "is_active = TRUE AND " + // Condición fundamental de Soft Delete
                        "(:minReputation IS NULL OR reputation_score >= :minReputation) AND " +
                        "(:maxReputation IS NULL OR reputation_score <= :maxReputation) AND " +
                        "(:minCo2Saved IS NULL OR JSON_EXTRACT(impact_metrics, '$.environmental.totalCo2SavedKg') >= :minCo2Saved) AND "
                        +
                        "(:minItemsDonated IS NULL OR JSON_EXTRACT(impact_metrics, '$.social.itemsDonated') >= :minItemsDonated)", nativeQuery = true)
        List<Companies> findByImpactFilters(
                        @Param("minReputation") Integer minReputation,
                        @Param("maxReputation") Integer maxReputation,
                        @Param("minCo2Saved") Double minCo2Saved,
                        @Param("minItemsDonated") Integer minItemsDonated);

        

}