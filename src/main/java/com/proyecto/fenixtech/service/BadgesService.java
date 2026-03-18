package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.repository.BadgesRepository;
import com.proyecto.fenixtech.dto.BadgesRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Badges;

@Service
public class BadgesService {
    @Autowired
    private BadgesRepository badgesRepository;

    @Autowired
    private ImageService imageService;


    @Transactional(readOnly = true)
    public List<Badges> findAllBadges() {
        return badgesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Badges> findByIsActiveTrue() {
        return badgesRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Badges findById(Integer id) {
        return badgesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insignia no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public Badges findByIdAndIsActiveTrue(Integer id) {
        return badgesRepository.findByBadgeIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insignia no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Badges> findByBadgeNameTrue(String name){
        return badgesRepository.findByBadgeNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    @Transactional(readOnly = true)
    public List<Badges> findByBadgeName(String name) {
        return badgesRepository.findByBadgeNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return badgesRepository.count();
    }

    @Transactional(readOnly = true)
    public Long countActive() {
        return badgesRepository.countByIsActiveTrue();
    }

    @Transactional
    public void deleteById(Integer id) {
        Badges badge = badgesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insignia no encontrada"));

        if (badge.getCompanyBadges() == null || badge.getCompanyBadges().isEmpty()) {
            badgesRepository.delete(badge);
        } else {
            badge.setIsActive(false);
            badgesRepository.save(badge);
        }
    }

    @Transactional
    public Badges save(BadgesRequestDTO dto) {
        badgesRepository.findByBadgeNameIgnoreCase(dto.getBadgeName())
                .ifPresent(badge -> {
                    throw new IllegalArgumentException("Ya existe una insignia con el nombre: " + badge.getBadgeName());
                });

        Badges badge = new Badges();
        badge.setBadgeName(dto.getBadgeName());
        if (dto.getIconUrl() != null && !dto.getIconUrl().isEmpty()) {
            String nameImg = imageService.guardarImagen(dto.getIconUrl());
            badge.setIconUrl("/fenixtech/uploads/" + nameImg);
        }
        badge.setIsActive(true);
        return badgesRepository.save(badge);
    }

    @Transactional
    public Badges update(Integer id, BadgesRequestDTO dto) {
        Badges badgesUpdate = badgesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la insignia con ID: " + id));

        badgesRepository.findByBadgeNameIgnoreCase(dto.getBadgeName())
            .ifPresent(existingBadge -> {
                if (!existingBadge.getBadgeId().equals(id)) {
                    throw new IllegalArgumentException("Ya existe otra insignia con el nombre: " + dto.getBadgeName());
                }
            });
        

        badgesUpdate.setBadgeName(dto.getBadgeName());
        if (dto.getIconUrl() != null && !dto.getIconUrl().isEmpty()) {
            String nameImg = imageService.guardarImagen(dto.getIconUrl());
            badgesUpdate.setIconUrl("/fenixtech/uploads/" + nameImg);
        }

        return badgesRepository.save(badgesUpdate);
    }

}
