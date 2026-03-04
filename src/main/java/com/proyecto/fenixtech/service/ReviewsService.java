package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Reviews;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ReviewsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Reviews> findAllReviews() {
        return reviewsRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Reviews> findReviewsByCompanyId(Integer companyId) {
        companiesRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + companyId));
        return reviewsRepository.findByTargetCompany_CompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public List<Reviews> findReviewsByUserId(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));
        return reviewsRepository.findByReviewer_UserId(userId);
    }

    @Transactional(readOnly = true)
    public Double getAverageRatingByCompanyId(Integer companyId) {
        companiesRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + companyId));
        return reviewsRepository.getAverageRatingByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Long countAllReviews() {
        return reviewsRepository.count();
    }
}
