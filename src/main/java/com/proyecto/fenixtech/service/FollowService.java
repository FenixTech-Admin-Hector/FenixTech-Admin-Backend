package com.proyecto.fenixtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.FollowsId;
import com.proyecto.fenixtech.repository.FollowRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import com.proyecto.fenixtech.model.Users;


@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public Long countFollowers(Integer userId) {
        if (!usersRepository.existsById(userId)) {
             throw new IllegalArgumentException("Usuario no encontrado con id: " + userId); 
        }
        return followRepository.countByFollowing_UserId(userId);
    }

    @Transactional(readOnly = true)
    public Long countFollowing(Integer userId) {
        if (!usersRepository.existsById(userId)) {
             throw new IllegalArgumentException("Usuario no encontrado con id: " + userId);
        }
        return followRepository.countByFollower_UserId(userId);
    }

    @Transactional
    public Boolean toggleUser(Integer followerId, Integer followingId){
        if(followerId.equals(followingId)){
            throw new IllegalArgumentException("No puedes seguirte a ti mismo");
        }
        
        FollowsId followsId = new FollowsId(followerId, followingId);

        // Dejar de seguir
        if(followRepository.existsById(followsId)){
            followRepository.deleteById(followsId);
            return false;
        }

        //Proceso de seguir
        Users follower = usersRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id: " + followerId));
        
        Users following = usersRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id: " + followingId));
        
        Follow follow = new Follow();
        follow.setId(followsId);
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
        return true;
    
    }


}
