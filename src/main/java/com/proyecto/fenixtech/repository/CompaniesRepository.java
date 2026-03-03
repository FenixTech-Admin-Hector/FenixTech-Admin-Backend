package com.proyecto.fenixtech.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Companies;


public interface CompaniesRepository extends JpaRepository<Companies, Integer> {

     // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
     * findAll()
     * findById(id)
     * 
     * count()
     * 
     * equals(User)
     * exist(User)
     * existById(id)
     */

    Optional <Companies> findByUser_UserId(Integer id);
    List<Companies> findByCompanyNameContainingIgnoringCase(String name);
    List<Companies> findByReputationScoreIsGreaterThan(Integer reputationScore);
    List<Companies> findByReputationScoreIsLessThan(Integer reputationScore);

    //Falta una consulta para el JSON de impact_metrics pero hasta que no esten definidas las claves del JSON no podrá estar definida
    

} 