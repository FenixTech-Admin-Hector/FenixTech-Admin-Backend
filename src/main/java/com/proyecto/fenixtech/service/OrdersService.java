package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Users;
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
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return ordersRepository.findByBuyer_UserId(id);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByStatus(OrderStatus status){
        return ordersRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByTotalAmountGreaterThan(Double amount){
        return ordersRepository.findByTotalAmountGreaterThan(amount);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByTotalAmountLessThan(Double amount){
        return ordersRepository.findByTotalAmountLessThan(amount);
    }

    @Transactional(readOnly = true)
    public List<Orders> findWithShipping(){
        return ordersRepository.findByRequiresShipping(true);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByShipping(Boolean requiresShipping){
        return ordersRepository.findByRequiresShipping(requiresShipping);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByOrderDate(Integer year){
        LocalDateTime yearStart = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime yearEnd = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999999999);
        return ordersRepository.findByOrderDateBetween(yearStart, yearEnd);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByOrderDateBetween(LocalDate dateStart, LocalDate dateEnd){
        if(dateStart.isAfter(dateEnd)){
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        LocalDateTime start = dateStart.atStartOfDay();
        LocalDateTime end = dateEnd.atTime(java.time.LocalTime.MAX);
        return ordersRepository.findByOrderDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByStatusAndRequiresShipping(Boolean requiresShipping, OrderStatus status){
        return ordersRepository.findByStatusAndRequiresShipping(requiresShipping, status);
    }


    @Transactional(readOnly = true)
    public Long count(){
        return ordersRepository.count();
    }

}
