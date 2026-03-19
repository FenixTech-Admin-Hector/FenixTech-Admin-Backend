package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CartItemsRepository;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;

import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.OrderRequestUpdateDTO;
import com.proyecto.fenixtech.dto.OrdersRequestDTO;
import com.proyecto.fenixtech.dto.ShipmentRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.CartItems;
import com.proyecto.fenixtech.model.OrderDetails;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.OrderStatus;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ReputationService reputationService;
    
    @Autowired
    private ShipmentsService shipmentsService;

    @Transactional(readOnly = true)
    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Orders findById(Integer id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !order.getBuyer().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para ver este pedido.");
        }

        return order;
    }

    @Transactional(readOnly = true)
    public List<Orders> findByBuyerId(Integer id) {
        Users currentUser = usersService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getUserId().equals(id)) {
            throw new AccessDeniedException("No puedes consultar el historial de compras de otro usuario.");
        }

        return ordersRepository.findByBuyer_UserId(id);
    }

    @Transactional(readOnly = true)
    public List<Orders> findByConditions(Double minAmount, Double maxAmount, LocalDate minDate, LocalDate maxDate,
            OrderStatus status, Boolean requiresShipping) {
        if (minDate != null && maxDate != null && minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        LocalDateTime _minDate = (minDate != null) ? minDate.atStartOfDay() : null;
        LocalDateTime _maxDate = (maxDate != null) ? maxDate.atTime(java.time.LocalTime.MAX) : null;

        if (minAmount != null && minAmount < 0) {
            throw new IllegalArgumentException("El importe mínimo no puede ser negativo");
        }
        if (maxAmount != null && maxAmount < 0) {
            throw new IllegalArgumentException("El importe máximo no puede ser negativo");
        }
        if (minAmount != null && maxAmount != null && minAmount > maxAmount) {
            throw new IllegalArgumentException("El importe mínimo no puede ser mayor al importe máximo");
        }

        String statusStr = (status != null) ? status.name() : null;

        return ordersRepository.findByConditions(minAmount, maxAmount, _minDate, _maxDate, statusStr, requiresShipping);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return ordersRepository.count();
    }

    @Transactional
    public Orders createOrderFromUserCart(OrdersRequestDTO dto) {
        Users buyer = usersService.getCurrentUser();

        List<CartItems> userCart = cartItemsRepository.findByUser_UserId(buyer.getUserId());
        if (userCart.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        Orders newOrder = new Orders();
        newOrder.setBuyer(buyer);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(OrderStatus.PENDING_PAYMENT);
        newOrder.setRequiresShipping(dto.getRequiresShipping());
        List<OrderDetails> detailsList = new ArrayList<>();
        Double totalCalculado = 0.0;

        for (CartItems item : userCart) {
            Products product = item.getProduct();

            if (product.getProductStatus() != ProductStatus.ACTIVE) {
                throw new IllegalArgumentException(
                        "El producto '" + product.getProductTitle() + "' ya no está activo.");
            }

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para: " + product.getProductTitle());
            }

            product.setStock(product.getStock() - item.getQuantity());
            if (product.getStock() == 0) {
                product.setProductStatus(ProductStatus.SOLD_OUT);
            }
            productsRepository.save(product);

            OrderDetails detail = new OrderDetails();
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(product.getPrice());
            detail.setOrder(newOrder);
            detailsList.add(detail);

            totalCalculado += (product.getPrice() * item.getQuantity());
        }

        newOrder.setOrderDetails(detailsList);
        newOrder.setTotalAmount(totalCalculado);

        Orders savedOrder = ordersRepository.save(newOrder);

        if (savedOrder.getRequiresShipping()) {
            ShipmentRequestDTO dtoShipment = new ShipmentRequestDTO();
            dtoShipment.setOrderId(savedOrder.getOrderId());
            dtoShipment.setCarrierId(dto.getCarrierId());
            dtoShipment.setShippingStreet(dto.getShippingStreet());
            dtoShipment.setShippingCity(dto.getShippingCity());
            dtoShipment.setShippingZipCode(dto.getShippingZipCode());
            dtoShipment.setShippingCountry(dto.getShippingCountry());

            shipmentsService.save(dtoShipment);
            
            // Refrescamos la orden para devolverla con el precio final correcto
            savedOrder = ordersRepository.findById(savedOrder.getOrderId()).get();
        }

        return savedOrder;
    }

    @Transactional
    public void deleteById(Integer id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el pedido: " + id));

        for (OrderDetails detail : order.getOrderDetails()) {
            Products product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());

            if (product.getProductStatus() == ProductStatus.SOLD_OUT) {
                product.setProductStatus(ProductStatus.ACTIVE);
            }

            productsRepository.save(product);
        }
        ordersRepository.deleteById(id);
    }

    @Transactional
    public Orders updateStatus(Integer id, OrderRequestUpdateDTO dto) {
        Users buyer = usersService.getCurrentUser();

        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (!order.getBuyer().getUserId().equals(buyer.getUserId()) && !buyer.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes permiso para modificar este pedido.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("No se puede modificar un pedido que ya ha sido cancelado.");
        }

        if (dto.getStatus() == OrderStatus.CANCELLED) {
            for (OrderDetails detail : order.getOrderDetails()) {
                Products product = detail.getProduct();
                product.setStock(product.getStock() + detail.getQuantity());

                if (product.getProductStatus() == ProductStatus.SOLD_OUT) {
                    product.setProductStatus(ProductStatus.ACTIVE);
                }
                productsRepository.save(product);
            }
        }

        if (dto.getStatus() == OrderStatus.PAID && order.getStatus() != OrderStatus.PAID) {

            cartItemsRepository.deleteByUser_UserId(order.getBuyer().getUserId());

            if (dto.getStatus() == OrderStatus.PAID) {
                for (OrderDetails detail : order.getOrderDetails()) {
                    Products product = detail.getProduct();
                    if (product.getCompany() != null) {
                        reputationService.proccessTransaction(
                                product.getCompany().getCompanyId(),
                                product,
                                detail.getQuantity());
                    }
                }
            }
        }

        order.setStatus(dto.getStatus());
        return ordersRepository.save(order);
    }

}
