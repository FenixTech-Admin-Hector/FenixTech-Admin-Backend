package com.proyecto.fenixtech.repository;

import com.proyecto.fenixtech.model.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    List<Reviews> findByTargetCompany_CompanyId(Integer companyId);

    List<Reviews> findByReviewer_UserId(Integer userId);

    Boolean existsByReviewer_UserIdAndTargetCompany_CompanyId(Integer userId, Integer companyId);


    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.targetCompany.companyId = :companyId")
    Double getAverageRatingByCompanyId(@Param("companyId") Integer companyId);
}
