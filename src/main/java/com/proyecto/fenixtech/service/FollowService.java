package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.FollowRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Follow;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.FollowRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private UsersService usersService;


    @Transactional
    public Boolean toggleFollow(FollowRequestDTO dto) {
        Users follower = usersService.getCurrentUser();

        Companies company = companiesRepository.findByCompanyIdAndIsActiveTrue(dto.getFollowing())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada o inactiva"));

        return followRepository.findByFollower_UserIdAndFollowing_CompanyId(follower.getUserId(), dto.getFollowing())
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
        Companies company = companiesRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Users currentUser = usersService.getCurrentUser();


        if (currentUser.getRole().name().equals("EMPRESA")) {
            if (!company.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new AccessDeniedException("No puedes ver los seguidores de otra empresa");
            }
        } else if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para ver esta lista de seguidores");
        }

        return followRepository.findByFollowing_CompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowingByUser(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(userId)) {
            throw new AccessDeniedException("No puedes ver la lista de seguidos de otro usuario");
        }

        return followRepository.findByFollower_UserId(userId);
    }
}