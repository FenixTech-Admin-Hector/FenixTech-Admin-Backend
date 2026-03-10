package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CommentsRepository;
import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.FollowRepository;
import com.proyecto.fenixtech.repository.OrderDetailsRepository;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.ProposalsRepository;
import com.proyecto.fenixtech.repository.ReviewsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.CompanyRegistrationDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.model.enums.ProductStatus;
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
    private ReviewsRepository reviewsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ProposalsRepository proposalsRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Transactional(readOnly = true)
    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Users> findByIsActiveTrue() {
        return usersRepository.findByIsActiveTrueAndRoleNot(Rol.ADMIN);
    }

    @Transactional(readOnly = true)
    public Users findByUsersId(Integer id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Users findByUserIdAndIsActiveTrue(Integer id) {
        return usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(id, Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o es privado"));
    }

    @Transactional(readOnly = true)
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con email:" + email));
    }

    @Transactional(readOnly = true)
    public Users findByEmailAndIsActiveTrue(String email) {
        return usersRepository.findByEmailAndIsActiveTrueAndRoleNot(email, Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o es privado"));
    }

    @Transactional(readOnly = true)
    public List<Users> findByRole(Rol rol) {
        return usersRepository.findByRole(rol);
    }

    @Transactional(readOnly = true)
    public List<Users> findByRoleAndIsActiveTrue(Rol rol) {
        if (rol == Rol.ADMIN) {
            throw new SecurityException(
                    "Operación no permitida: No se pueden buscar administradores por esta vía pública.");
        }
        return usersRepository.findByRoleAndIsActiveTrue(rol);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtOrderByDesc() {
        return usersRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtAndIsActiveTrueOrderByDesc() {
        return usersRepository.findByIsActiveTrueAndRoleNotOrderByCreatedAtDesc(Rol.ADMIN);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtOrderByAsc() {
        return usersRepository.findAllByOrderByCreatedAtAsc();
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtAndIsActiveTrueOrderByAsc() {
        return usersRepository.findByIsActiveTrueAndRoleNotOrderByCreatedAtAsc(Rol.ADMIN);
    }

    @Transactional(readOnly = true)
    // Se usa el mismo metodo para buscar por año y para buscar por rango de fechas
    public List<Users> findByCreatedAt(Integer year) {
        // Crea un LocalDateTime a partir de un año para revsar desde el 1 de enero a
        // las 00:00:00 de ese año
        LocalDateTime inicioDelAno = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        // Crea un LocalDateTime a partir de un año para revisar hasta el 31 de
        // diciembre a las 23:59:59 de ese año con los maximos milisegundos
        LocalDateTime finDelAno = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999999999);
        return usersRepository.findByCreatedAtBetween(inicioDelAno, finDelAno);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtAndIsActiveTrue(Integer year) {
        LocalDateTime inicioDelAno = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime finDelAno = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999999999);
        return usersRepository.findByIsActiveTrueAndRoleNotAndCreatedAtBetween(Rol.ADMIN, inicioDelAno, finDelAno);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtBetween(LocalDate dateStart, LocalDate dateEnd) {
        if (dateStart.isAfter(dateEnd)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        // Convierte el LocalDate a LocalDateTime con la fecha con hora 00:00:00
        LocalDateTime startDateTime = dateStart.atStartOfDay();
        // Convierte el LocalDate en LocalDateTime con la fecha con hora 23:59:59 con
        // los maximos milisegundos
        LocalDateTime endDateTime = dateEnd.atTime(java.time.LocalTime.MAX);
        return usersRepository.findByCreatedAtBetween(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<Users> findByCreatedAtBetweenAndIsActiveTrue(LocalDate dateStart, LocalDate dateEnd) {
        if (dateStart.isAfter(dateEnd)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        LocalDateTime startDateTime = dateStart.atStartOfDay();
        LocalDateTime endDateTime = dateEnd.atTime(java.time.LocalTime.MAX);
        return usersRepository.findByIsActiveTrueAndRoleNotAndCreatedAtBetween(Rol.ADMIN, startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return usersRepository.count();
    }

    @Transactional
    public Users save(Users user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setIsActive(true);

        if (user.getRole() == Rol.ADMIN) {
            throw new SecurityException(
                    "Operación no permitida: No se pueden crear cuentas de Administrador por esta vía.");
        }
        if (user.getRole() == null) {
            user.setRole(Rol.PARTICULAR);
        }

        if (user.getCompany() != null) {
            user.getCompany().setUser(user);
        }

        if (user.getAddresses() != null) {
            for (int i = 0; i < user.getAddresses().size(); i++) {
                user.getAddresses().get(i).setUser(user);
            }
        }

        return usersRepository.save(user);
    }

    @Transactional
    public Users createAdmin(Users user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setIsActive(true);
        user.setRole(Rol.ADMIN);

        return usersRepository.save(user);
    }

    @Transactional
    public Users update(Integer id, Users user) {

        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID: " + id));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUserImg(user.getUserImg());

        if (!existingUser.getEmail().equals(user.getEmail()) &&
                usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado por otro usuario");
        }
        existingUser.setEmail(user.getEmail());

        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            // En el futuro, se hará distinto al usar BYCrypt
            existingUser.setPasswordHash(user.getPasswordHash());
        }

        return usersRepository.save(existingUser);
    }

    @Transactional
    public void delete(Integer id) {
        Users user = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(id, Rol.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getRole() == Rol.EMPRESA && user.getCompany() != null) {
            Integer companyId = user.getCompany().getCompanyId();

            // Limpiezas manuales necesarias
            productsRepository.deleteCartItemsByCompanyId(companyId);
            reviewsRepository.deleteAll(user.getCompany().getReviews());
            productsRepository.hideAllByCompanyId(companyId);

            // Al ponerlo a null, orphanRemoval = true se encarga de borrar la empresa
            // físicamente
            user.setCompany(null);
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
    public Users registerCompany(CompanyRegistrationDTO dto) {
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserImg(dto.getUserImg());
        user.setRole(Rol.EMPRESA);
        user.setIsActive(true);

        // 2. Creamos la Empresa vinculada
        Companies company = new Companies();
        company.setCompanyName(dto.getCompanyName());
        company.setCif(dto.getCif());
        company.setCompanyImg(dto.getCompanyImg());
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

        // 3. Vinculación bidireccional CRÍTICA
        user.setCompany(company);
        company.setUser(user);

        // 4. Guardar
        return usersRepository.save(user);
    }
}
