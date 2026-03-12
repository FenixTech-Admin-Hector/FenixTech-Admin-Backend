package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

        Optional<Follow> findByFollower_UserIdAndFollowing_CompanyId(Integer userId, Integer companyId);

        Long countByFollowing_CompanyId(Integer companyId);

        Long countByFollower_UserId(Integer userId);

        // Lista de particulares que siguen a una empresa
        List<Follow> findByFollowing_CompanyId(Integer companyId);

        // Lista de empresas a las que sigue un particular
        List<Follow> findByFollower_UserId(Integer userId);

}
