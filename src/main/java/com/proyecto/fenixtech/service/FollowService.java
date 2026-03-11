package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.FollowDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.FollowsId;
import com.proyecto.fenixtech.repository.FollowRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import com.proyecto.fenixtech.model.Users;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Follow> getActiveFollowers(Integer userId) {
        validateUserExists(userId);
        return followRepository.findActiveFollowers(userId);
    }

    @Transactional(readOnly = true)
    public List<Follow> getActiveFollowing(Integer userId) {
        validateUserExists(userId);
        return followRepository.findActiveFollowing(userId);
    }

    @Transactional(readOnly = true)
    public Long countActiveFollowers(Integer userId) {
        validateUserExists(userId);
        return followRepository.countByFollowing_UserIdAndFollower_IsActiveTrue(userId);
    }

    @Transactional(readOnly = true)
    public Long countActiveFollowing(Integer userId) {
        validateUserExists(userId);
        return followRepository.countByFollower_UserIdAndFollowing_IsActiveTrue(userId);
    }

  
    @Transactional
    public Boolean toggleUser(FollowDTO dto) {
        Integer followerId = dto.getFollowerId();
        Integer followingId = dto.getFollowing();

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo");
        }

        Users follower = usersRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario seguidor no encontrado"));
        
        Users following = usersRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario a seguir no encontrado"));

        if (!follower.getIsActive() || !following.getIsActive()) {
            throw new IllegalArgumentException("No se pueden realizar interacciones con cuentas inactivas");
        }

        FollowsId followsId = new FollowsId(followerId, followingId);

        if (followRepository.existsById(followsId)) {
            followRepository.deleteById(followsId);
            return false;
        }

        Follow follow = new Follow();
        follow.setId(followsId);
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
        return true;
    }

    
    private void validateUserExists(Integer userId) {
        if (!usersRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }
    }
}