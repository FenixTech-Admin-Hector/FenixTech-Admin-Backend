package com.proyecto.fenixtech.admin.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Importamos los Modelos compartidos
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;

// Importamos servicios y repositorios del grupo que necesitamos usar
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.CommentsRepository;
import com.proyecto.fenixtech.service.ImageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

@Service
public class AdminUsersService {
    
    @Autowired
    private AdminUsersRepository adminUsersRepository; // TU repositorio
    
    // Repositorios del grupo para limpiar cascadas
    @Autowired private ProductsRepository productsRepository;
    @Autowired private PostsRepository postsRepository;
    @Autowired private ProposalsRepository proposalsRepository;
    @Autowired private CommentsRepository commentsRepository;
    @Autowired private ImageService imageService;

    // 1. MOSTRAR Y FILTRAR USUARIOS
    @Transactional(readOnly = true)
    public List<Users> findUsers(Rol role, Boolean active, LocalDate start, LocalDate end, String direction) {
        LocalDateTime startDT = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endDT = (end != null) ? end.atTime(java.time.LocalTime.MAX) : null;
        String roleStr = (role != null) ? role.name() : null;

        List<Users> results = adminUsersRepository.findUsersByFiltersNative(roleStr, active, startDT, endDT);

        if ("desc".equalsIgnoreCase(direction)) {
            Collections.reverse(results);
        }
        return results;
    }

    // 2. BANEAR USUARIO (Borrado Lógico)
    @Transactional
    public void delete(Integer id) {
        Users user = adminUsersRepository.findByUserIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getRole() == Rol.EMPRESA && user.getCompany() != null) {
            Companies company = user.getCompany();
            company.setIsActive(false);
            productsRepository.deleteCartItemsByCompanyId(company.getCompanyId());
            productsRepository.hideAllByCompanyId(company.getCompanyId());
        }

        cleanUserInteractions(user);
        user.setIsActive(false);
        user.setDeletedAt(LocalDateTime.now());

        adminUsersRepository.save(user);
    }

    private void cleanUserInteractions(Users user) {
        if (user.getPosts() != null && !user.getPosts().isEmpty()) {
            postsRepository.deleteAll(user.getPosts());
            user.getPosts().clear();
        }
        if (user.getComments() != null && !user.getComments().isEmpty()) {
            commentsRepository.deleteAll(user.getComments());
            user.getComments().clear();
        }
        if (user.getProposals() != null && !user.getProposals().isEmpty()) {
            proposalsRepository.deleteAll(user.getProposals());
            user.getProposals().clear();
        }
    }

    // 3. DESBANEAR USUARIO
    @Transactional
    public void unbanUser(Integer id) {
        Users user = adminUsersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setIsActive(true);
        user.setDeletedAt(null);

        if (user.getRole() == Rol.EMPRESA && user.getCompany() != null) {
            user.getCompany().setIsActive(true);
        }

        adminUsersRepository.save(user);
    }

    // 4. EDITAR USUARIO COMO ADMIN
    @Transactional
    public AdminUserResponseDTO updateUserAsAdmin(Integer id, AdminUserUpdateDTO dto) {
        Users user = adminUsersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            if (adminUsersRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email '" + dto.getEmail() + "' ya está registrado.");
            }
            user.setEmail(dto.getEmail());
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        
        if (dto.getUserImg() != null && !dto.getUserImg().isEmpty()) {
            String nameImg = imageService.guardarImagen(dto.getUserImg());
            user.setUserImg("/fenixtech/uploads/" + nameImg);
        }
        
        user.setDescription(dto.getDescription());
        adminUsersRepository.save(user);

        return AdminUserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userImg(user.getUserImg())
                .role(user.getRole())
                .description(user.getDescription())
                .build();
    }
}