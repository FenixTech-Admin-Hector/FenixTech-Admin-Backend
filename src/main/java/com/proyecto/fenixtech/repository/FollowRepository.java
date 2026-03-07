package com.proyecto.fenixtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.FollowsId;

public interface FollowRepository extends JpaRepository<Follow, FollowsId>{
    Long countByFollower_UserId(Integer userId);
    Long countByFollowing_UserId(Integer userId);

    // List<Follow> findByFollowing_UserId(Integer userId);
    // List<Follow> findByFollower_UserId(Integer userId);

}
