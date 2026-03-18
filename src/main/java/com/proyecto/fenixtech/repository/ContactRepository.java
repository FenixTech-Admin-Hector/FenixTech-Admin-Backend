package com.proyecto.fenixtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
    
}
