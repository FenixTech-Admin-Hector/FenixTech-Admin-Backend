package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.ContactRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Contact;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.CategoriesRepository;
import com.proyecto.fenixtech.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}") 
    private String adminEmail;

    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Contact findById(Integer id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de contacto no encontrada con id: " + id));
    }


    @Transactional
    public Contact createContactAndNotify(ContactRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría seleccionada no existe"));

        Contact contact = new Contact();
        contact.setTitle(dto.getTitle());
        contact.setDescription(dto.getDescription());
        contact.setCategory(category);
        contact.setUser(currentUser);
        
        Contact savedContact = contactRepository.save(contact);

        emailService.sendContactAdminNotification(adminEmail, savedContact, currentUser, category.getName());

        return savedContact;
    }
}