package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.FollowsId;

public interface FollowRepository extends JpaRepository<Follow, FollowsId> {

        Long countByFollowing_UserIdAndFollower_IsActiveTrue(Integer userId);

        Long countByFollower_UserIdAndFollowing_IsActiveTrue(Integer userId);

        @Query(value = "SELECT f.* FROM follows f " +
                        "INNER JOIN users u ON f.follower_id = u.user_id " +
                        "WHERE f.following_id = :userId AND u.is_active = TRUE", nativeQuery = true)
        List<Follow> findActiveFollowers(@Param("userId") Integer userId);

        @Query(value = "SELECT f.* FROM follows f " +
                        "INNER JOIN users u ON f.following_id = u.user_id " +
                        "WHERE f.follower_id = :userId AND u.is_active = TRUE", nativeQuery = true)
        List<Follow> findActiveFollowing(@Param("userId") Integer userId);
        // List<Follow> findByFollowing_UserId(Integer userId);
        // List<Follow> findByFollower_UserId(Integer userId);

}
