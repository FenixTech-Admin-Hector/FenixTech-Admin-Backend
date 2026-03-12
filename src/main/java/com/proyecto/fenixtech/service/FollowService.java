package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.FollowRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.FollowRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Transactional
    public Boolean toggleFollow(FollowRequestDTO dto) {
        Users follower = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(dto.getFollowerId(), Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario seguidor no encontrado o no esta activo"));

        if (follower.getRole() != Rol.PARTICULAR) {
            throw new IllegalArgumentException("Solo los usuarios particulares activos pueden seguir empresas.");
        }

        Companies company = companiesRepository.findByCompanyIdAndIsActiveTrue(dto.getFollowing())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada o inactiva"));


        return followRepository.findByFollower_UserIdAndFollowing_CompanyId(dto.getFollowerId(), dto.getFollowing())
                .map(follow -> {
                    followRepository.delete(follow);
                    return false; 
                })
                .orElseGet(() -> {
                    Follow newFollow = new Follow();
                    newFollow.setFollower(follower);
                    newFollow.setFollowing(company);
                    followRepository.save(newFollow);
                    return true; 
                });
    }

    @Transactional(readOnly = true)
    public Long countFollowersByCompany(Integer companyId) {
        return followRepository.countByFollowing_CompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Long countFollowingByUser(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        return followRepository.countByFollower_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowersByCompany(Integer companyId) {
        companiesRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));
        return followRepository.findByFollowing_CompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowingByUser(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return followRepository.findByFollower_UserId(userId);
    }
}