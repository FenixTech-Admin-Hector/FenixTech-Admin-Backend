package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CommentsRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.CompanyRequestDTO;
import com.proyecto.fenixtech.dto.ParticularRequestDTO;
import com.proyecto.fenixtech.dto.PasswordUpdateDTO;
import com.proyecto.fenixtech.dto.UserResponseDTO;
import com.proyecto.fenixtech.dto.UserUpdateDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Addresses;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.model.json.EnvironmentalMetrics;
import com.proyecto.fenixtech.model.json.ImpactMetrics;
import com.proyecto.fenixtech.model.json.SocialMetrics;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ProposalsRepository proposalsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;


    @Transactional(readOnly = true)
    public List<Users> findUsers(Rol role, Boolean active, LocalDate start, LocalDate end, String direction) {
        LocalDateTime startDT = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endDT = (end != null) ? end.atTime(java.time.LocalTime.MAX) : null;

        String roleStr = (role != null) ? role.name() : null;

        List<Users> results = usersRepository.findUsersByFiltersNative(roleStr, active, startDT, endDT);

        if ("desc".equalsIgnoreCase(direction)) {
            Collections.reverse(results);
        }

        return results;
    }

    @Transactional(readOnly = true)
    public Users findById(Integer id, boolean onlyActive) {
        if (onlyActive) {
            return usersRepository.findByUserIdAndIsActiveTrue(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo"));
        }
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Users findByEmail(String email, boolean onlyActive) {
        if (onlyActive) {
            return usersRepository.findByEmailAndIsActiveTrue(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo"));
        }
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Transactional(readOnly = true)
    public Users getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usersRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión inválida o usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMyProfile() {
        Users user = getCurrentUser();

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userImg(user.getUserImg())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public UserResponseDTO updateMyProfile(UserUpdateDTO dto) {
        Users user = getCurrentUser();

        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException(
                        "El email '" + dto.getEmail() + "' ya está registrado por otro usuario.");
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
        usersRepository.save(user);

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userImg(user.getUserImg())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void updatePassword(PasswordUpdateDTO dto) {
        Users user = getCurrentUser();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        usersRepository.save(user);
    }

    @Transactional
    public void delete(Integer id) {
        Users user = usersRepository.findByUserIdAndIsActiveTrue(id)
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

        usersRepository.save(user); // Aquí se procesa el borrado de la empresa y el update del usuario
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

    @Transactional
    public Users registerParticular(ParticularRequestDTO dto) {
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        if (dto.getUserImg() != null && !dto.getUserImg().isEmpty()) {
            String nameImg = imageService.guardarImagen(dto.getUserImg());
            user.setUserImg("/fenixtech/uploads/" + nameImg);
        }
        user.setRole(Rol.PARTICULAR);
        user.setIsActive(true);

        return usersRepository.save(user);
    }

    @Transactional
    public Users registerCompany(CompanyRequestDTO dto) {
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(Rol.EMPRESA);
        user.setIsActive(true);

        // 2. Creamos la Empresa vinculada
        Companies company = new Companies();
        company.setCompanyName(dto.getCompanyName());
        company.setCif(dto.getCif());
        
        company.setReputationScore(0);

        // Inicializacion de JSON
        SocialMetrics socialMetrics = new SocialMetrics();
        socialMetrics.setItemsDonated(0);
        socialMetrics.setItemsSoldDiscounted(0);
        EnvironmentalMetrics environmentalMetrics = new EnvironmentalMetrics();
        environmentalMetrics.setTotalCo2SavedKg(0.00);
        environmentalMetrics.setTotalEwasteSavedKg(0.00);
        ImpactMetrics impactMetrics = new ImpactMetrics();
        impactMetrics.setEnvironmental(environmentalMetrics);
        impactMetrics.setSocial(socialMetrics);
        company.setImpactMetrics(impactMetrics);

        // Vincular la dirección
        Addresses fiscalAddress = new Addresses();
        fiscalAddress.setStreet(dto.getStreet());
        fiscalAddress.setCity(dto.getCity());
        fiscalAddress.setZipCode(dto.getZipCode());
        fiscalAddress.setRegion(dto.getRegion());
        fiscalAddress.setCountry(dto.getCountry());
        fiscalAddress.setUser(user);

        // 3. Añadir la dirección a la lista del usuario
        user.getAddresses().add(fiscalAddress);

        // 3. Vinculación bidireccional CRÍTICA
        user.setCompany(company);
        company.setUser(user);

        // 4. Guardar
        return usersRepository.save(user);
    }
}
