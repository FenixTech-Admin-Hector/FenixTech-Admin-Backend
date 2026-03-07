package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CompaniesRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Companies;
import com.proyecto.fenixtech.model.json.EnvironmentalMetrics;
import com.proyecto.fenixtech.model.json.ImpactMetrics;
import com.proyecto.fenixtech.model.json.SocialMetrics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CompaniesService {
    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<Companies> findAllCompanies(){
        return companiesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Companies findById(Integer id){
        return companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id:" + id));
    }

    @Transactional(readOnly = true)
    public Companies findByUserId(Integer id){
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id:" + id));

        return companiesRepository.findByUser_UserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario" + id + "no está asociado a ninguna empresa"));
    }

    @Transactional(readOnly = true)
    public List<Companies> findByCompanyName(String name){
        return companiesRepository.findByCompanyNameContainingIgnoringCase(name);
    }

    @Transactional(readOnly = true)
    public List<Companies> findByImpactFilters(Integer minReputation, Integer maxReputation, Double minCo2Saved, Integer minItemsDonated){
        if(minReputation != null && minReputation < 0){
            throw new IllegalArgumentException("La calificación mínima no puede ser negativa");
        }
        if(maxReputation != null && maxReputation < 0){
            throw new IllegalArgumentException("La calificación máxima no puede ser negativa");
        }
        if(minReputation != null && maxReputation != null && minReputation > maxReputation){
            throw new IllegalArgumentException("La calificación mínima no puede ser superior a la calificación máxima");
        }
        if(minCo2Saved != null && minCo2Saved < 0){
            throw new IllegalArgumentException("El ahorro mínimo no puede ser negativo");
        }
        if(minItemsDonated != null && minItemsDonated < 0){
            throw new IllegalArgumentException("El mínimo de artículos donados no puede ser negativo");
        }

        return companiesRepository.findByImpactFilters(minReputation, maxReputation, minCo2Saved, minItemsDonated);
    }

    @Transactional(readOnly = true)
    public List<Companies> findTop3ByReputationScore(){
        return companiesRepository.findTop3ByOrderByReputationScoreDesc();
    }

    @Transactional(readOnly = true)
    public Long count(){
        return companiesRepository.count();
    }

    @Transactional
    public Companies save(Companies company){
        if(company.getUser() == null || company.getUser().getUserId() == null){
            throw new IllegalArgumentException("La empresa debe estar asociada a un usuario válido con ID.");
        }

        usersRepository.findById(company.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario con ID " + company.getUser().getUserId() + " no existe"));
        
        company.setReputationScore(0);
        company.setImpactMetrics(new ImpactMetrics(
            new EnvironmentalMetrics(),
            new SocialMetrics()
        ));

        return companiesRepository.save(company);
    }

    @Transactional 
    public void deleteById(Integer id){
        if(!companiesRepository.existsById(id)){
            throw new IllegalArgumentException("No existe la empresa con id: " + id + " para eliminar");
        }
        companiesRepository.deleteById(id);
    }

    @Transactional
    public Companies update(Integer id, Companies company){
        Companies companyUpdate = companiesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la empresa con ID: " + id));

        companyUpdate.setCompanyName(company.getCompanyName());
        companyUpdate.setCif(company.getCif());
        companyUpdate.setCompanyImg(company.getCompanyImg());


        return companiesRepository.save(companyUpdate);
    }





}
