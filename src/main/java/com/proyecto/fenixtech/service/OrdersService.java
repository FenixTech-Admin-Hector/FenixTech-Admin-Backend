package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.CartItemsRepository;
import com.proyecto.fenixtech.repository.OrdersRepository;
import com.proyecto.fenixtech.repository.ProductsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

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
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ReputationService reputationService;

    @Transactional(readOnly = true)
    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Orders findById(Integer id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Orders> findByBuyerId(Integer id) {
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
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
    public Orders createOrderFromUserCart(Integer userId, Boolean requiresShipping) {
        Users buyer = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede crear el pedido: El usuario con ID " + userId + " no existe"));

        List<CartItems> userCart = cartItemsRepository.findByUser_UserId(userId);

        if (userCart.isEmpty()) {
            throw new ResourceNotFoundException("El carrito está vacío, no se puede crear el pedido.");
        }

        Orders newOrder = new Orders();
        newOrder.setBuyer(buyer);
        newOrder.setRequiresShipping(requiresShipping);
        List<OrderDetails> detailsList = new ArrayList<>();
        Double totalCalculado = 0.0;

        for (CartItems item : userCart) {
            Products product = item.getProduct();

            if (product.getProductStatus() != ProductStatus.ACTIVE) {
                throw new IllegalArgumentException("El producto '" + product.getProductTitle() +
                        "' ya no está disponible. Por favor, elimínalo de tu carrito.");
            }

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getProductTitle());
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

            if (product.getCompany() != null) {
                reputationService.proccessTransaction(product.getCompany().getCompanyId(), product, item.getQuantity());
            }
        }

        newOrder.setOrderDetails(detailsList);
        newOrder.setTotalAmount(totalCalculado);

        Orders savedOrder = ordersRepository.save(newOrder);

        cartItemsRepository.deleteByUser_UserId(userId);

        return savedOrder;
    }

    @Transactional
    public void deleteById(Integer id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el pedido: " + id));

        for (OrderDetails detail : order.getOrderDetails()) {
            Products product = detail.getProduct();
            // Se resetea el stock si se cancela un pedido
            product.setStock(product.getStock() + detail.getQuantity());

            // Se resetea el estado del producto (en el caso de agotarse) si se cancela un
            // pedido
            if (product.getProductStatus() == ProductStatus.SOLD_OUT) {
                product.setProductStatus(ProductStatus.ACTIVE);
            }

            productsRepository.save(product);
        }
        ordersRepository.deleteById(id);
    }

    @Transactional
    public Orders updateStatus(Integer id, OrderStatus newStatus) {
        Orders order = findById(id);
        order.setStatus(newStatus);
        return ordersRepository.save(order);
    }

}
