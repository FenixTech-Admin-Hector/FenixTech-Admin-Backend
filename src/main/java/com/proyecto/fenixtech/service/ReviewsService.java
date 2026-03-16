package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.ReviewsRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Reviews;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ReviewsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private ReputationService reputationService;

    @Autowired
    private UsersService usersService;

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

    @Transactional
    public Reviews save(ReviewsRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        if (currentUser.getRole() == Rol.EMPRESA || currentUser.getRole() == Rol.ADMIN) {
            throw new IllegalArgumentException("Solo los usuarios particulares pueden dejar reseñas.");
        }

        Companies targetCompany = companiesRepository.findById(dto.getCompanyId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("La empresa con ID " + dto.getCompanyId() + " no existe"));

        if (!targetCompany.getIsActive()) {
            throw new IllegalArgumentException("No se pueden dejar reseñas a una empresa inactiva.");
        }

        if (reviewsRepository.existsByReviewer_UserIdAndTargetCompany_CompanyId(currentUser.getUserId(),
                dto.getCompanyId())) {
            throw new IllegalArgumentException("Ya has dejado una reseña para esta empresa.");
        }

        Reviews review = new Reviews();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewer(currentUser);
        review.setTargetCompany(targetCompany);

        reputationService.processReviewScore(targetCompany.getCompanyId(), dto.getRating());
        return reviewsRepository.save(review);
    }

    @Transactional
    public void deleteById(Integer id) {
        Reviews review = reviewsRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe la review con id: " + id + " para eliminar"));

        Users currentUser = usersService.getCurrentUser();

        if (!review.getReviewer().getUserId().equals(currentUser.getUserId()) &&
                currentUser.getRole() != Rol.ADMIN) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta reseña.");
        }

        reputationService.deleteReviewScore(review.getTargetCompany().getCompanyId(), review.getRating());

        reviewsRepository.deleteById(id);
    }

    @Transactional
    public Reviews update(Integer id, ReviewsRequestDTO dto) {
        Reviews reviewUpdate = reviewsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la review con ID: " + id));

        Users currentUser = usersService.getCurrentUser();

        if (!reviewUpdate.getReviewer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes editar una reseña que no te pertenece.");
        }

        if (!reviewUpdate.getReviewer().getUserId().equals(dto.getUserId())) {
            throw new IllegalArgumentException("No puedes editar una reseña que no te pertenece.");
        }

        if (!reviewUpdate.getRating().equals(dto.getRating())) {
            reputationService.updateReviewScore(
                    reviewUpdate.getTargetCompany().getCompanyId(),
                    reviewUpdate.getRating(),
                    dto.getRating());
        }

        reviewUpdate.setRating(dto.getRating());
        reviewUpdate.setComment(dto.getComment());

        return reviewsRepository.save(reviewUpdate);
    }
}
