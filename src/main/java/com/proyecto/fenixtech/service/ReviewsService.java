package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Reviews;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.ReviewsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public Reviews save(Reviews review) {
        if (review.getReviewer() == null || review.getReviewer().getUserId() == null) {
            throw new IllegalArgumentException("La review debe estar asociada a un usuario válido con ID.");
        }
        if (review.getTargetCompany() == null || review.getTargetCompany().getCompanyId() == null) {
            throw new IllegalArgumentException("La review debe estar asociada a una empresa válida con ID.");
        }

        Users reviewer = usersRepository.findById(review.getReviewer().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario con ID " + review.getReviewer().getUserId() + " no existe"));

        if (reviewer.getRole() == Rol.EMPRESA) {
            throw new IllegalArgumentException("El rol para hacer reviews del usuario tiene que ser: " + Rol.PARTICULAR.name());
        }


        Companies targetCompany = companiesRepository.findById(review.getTargetCompany().getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La empresa con ID " + review.getTargetCompany().getCompanyId() + " no existe"));

        review.setReviewer(reviewer);
        review.setTargetCompany(targetCompany);
        //Valorar implementar una columna updated_at para guardar fechas de actualizacion

        reputationService.processReviewScore(review.getTargetCompany().getCompanyId(), review.getRating());

        return reviewsRepository.save(review);
    }

    @Transactional
    public void deleteById(Integer id) {
        Reviews review = reviewsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe la review con id: " + id + " para eliminar"));

        reputationService.deleteReviewScore(review.getTargetCompany().getCompanyId(), review.getRating());

        reviewsRepository.deleteById(id);
    }

    @Transactional
    public Reviews update (Integer id, Reviews review){
        Reviews reviewUpdate = reviewsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la review con ID: " + id));

        if(!reviewUpdate.getRating().equals(review.getRating())){
            reputationService.updateReviewScore(reviewUpdate.getTargetCompany().getCompanyId(), reviewUpdate.getRating(), review.getRating());
        }

        reviewUpdate.setRating(review.getRating());
        reviewUpdate.setComment(review.getComment());
        reviewUpdate.setCreatedAt(LocalDateTime.now());

        return reviewsRepository.save(reviewUpdate);
    }
}
