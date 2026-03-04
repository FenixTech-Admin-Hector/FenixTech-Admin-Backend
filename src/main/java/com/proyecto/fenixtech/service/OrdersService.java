package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Orders;

import com.proyecto.fenixtech.model.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
    @Autowired 
    private OrdersRepository ordersRepository;

    @Autowired
    private UsersRepository usersRepository;


    @Transactional(readOnly = true)
    public List<Orders> findAllOrders(){
        return ordersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Orders findById(Integer id){
        return ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Orders> findByBuyerId(Integer id){
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return ordersRepository.findByBuyer_UserId(id);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByConditions(Double minAmount, Double maxAmount, LocalDate minDate, LocalDate maxDate, OrderStatus status, Boolean requiresShipping){
        if(minDate != null && maxDate != null && minDate.isAfter(maxDate)){
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        LocalDateTime _minDate = (minDate != null) ? minDate.atStartOfDay() : null;
        LocalDateTime _maxDate = (maxDate != null) ? maxDate.atTime(java.time.LocalTime.MAX) : null;

        if(minAmount != null && minAmount < 0){
            throw new IllegalArgumentException("El importe mínimo no puede ser negativo");
        }
        if(maxAmount != null && maxAmount < 0){
            throw new IllegalArgumentException("El importe máximo no puede ser negativo");
        }
        if(minAmount != null && maxAmount != null && minAmount > maxAmount){
            throw new IllegalArgumentException("El importe mínimo no puede ser mayor al importe máximo");
        }

        String statusStr = (status != null) ? status.name().toLowerCase() : null;

        return ordersRepository.findByConditions(minAmount, maxAmount, _minDate, _maxDate, statusStr, requiresShipping);
    }



    @Transactional(readOnly = true)
    public Long count(){
        return ordersRepository.count();
    }

}
