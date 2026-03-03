package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.BadgesRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Badges;

@Service
public class BadgesService {
    @Autowired
    private BadgesRepository badgesRepository;

    @Transactional(readOnly = true)
    public List<Badges> findAllBadges() {
        return badgesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Badges findById(Integer id) {
        return badgesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insignia no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Badges> findByBadgeName(String name) {
        return badgesRepository.findByBadgeNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return badgesRepository.count();
    }

}
