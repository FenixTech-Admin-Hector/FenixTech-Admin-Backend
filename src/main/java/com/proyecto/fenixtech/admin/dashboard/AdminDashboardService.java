package com.proyecto.fenixtech.admin.dashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importamos repositorios
import com.proyecto.fenixtech.admin.user.AdminUsersRepository;
import com.proyecto.fenixtech.admin.product.AdminProductsRepository;
import com.proyecto.fenixtech.admin.proposals.AdminProposalsRepository;
import com.proyecto.fenixtech.admin.order.AdminOrdersRepository;

import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.model.enums.ProductStatus;
import com.proyecto.fenixtech.model.enums.ProposalStatus; // Importamos Enum de proposals


@Service
public class AdminDashboardService {

    @Autowired
    private AdminUsersRepository adminUsersRepository;
    @Autowired
    private AdminProductsRepository adminProductsRepository;
    @Autowired
    private AdminProposalsRepository adminProposalsRepository;
    @Autowired
    private AdminOrdersRepository adminOrdersRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDTO getDashboardStats() {
        // 1. Cálculos de Usuarios
        long particulars = adminUsersRepository.countByRole(Rol.PARTICULAR);
        long companies = adminUsersRepository.countByRole(Rol.EMPRESA);

        // 2. Cálculos de Productos
        long activeProducts = adminProductsRepository.countByProductStatus(ProductStatus.ACTIVE);
        long hiddenProducts = adminProductsRepository.countByProductStatus(ProductStatus.HIDDEN);

        // 3. Cálculos de Solicitudes (🚀 NUEVO)
        long openProposals = adminProposalsRepository.countByStatus(ProposalStatus.OPEN);
        long fulfilledProposals = adminProposalsRepository.countByStatus(ProposalStatus.FULFILLED);

        // 4. Cálculos de Pedidos (🚀 NUEVO)
        // La BD guarda 1 y 0, que en Java se traduce automáticamente a true (envío) y
        // false (recogida)
        long shippingOrders = adminOrdersRepository.countByRequiresShipping(true);
        long pickupOrders = adminOrdersRepository.countByRequiresShipping(false);

        // 5. Ingresos
        Double totalRev = adminOrdersRepository.sumTotalAmountByStatus("COMPLETED");

        return AdminDashboardStatsDTO.builder()
                .totalUsers(particulars + companies)
                .totalProducts(activeProducts + hiddenProducts)
                .totalProposals(adminProposalsRepository.count())
                .totalOrders(adminOrdersRepository.count())
                .totalRevenue(totalRev != null ? totalRev : 0.0)

                .usersByRole(Map.of("Particulares", particulars, "Empresas", companies))
                .productsByStatus(Map.of("Activos", activeProducts, "Inactivos", hiddenProducts))
                // AÑADIMOS ESTO AL BUILDER PARA QUE LLEGUE AL FRONTEND
                .proposalsByStatus(Map.of("Pendientes", openProposals, "Aceptadas", fulfilledProposals))
                .ordersByType(Map.of("Envío", shippingOrders, "Recogida", pickupOrders))
                .build();
    }
}