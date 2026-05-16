package com.proyecto.fenixtech.admin.dashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importamos TUS repositorios
import com.proyecto.fenixtech.admin.user.AdminUsersRepository;
import com.proyecto.fenixtech.admin.product.AdminProductsRepository;
import com.proyecto.fenixtech.admin.proposal.AdminProposalsRepository;
import com.proyecto.fenixtech.admin.order.AdminOrdersRepository;

import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.model.enums.ProductStatus;

@Service
public class AdminDashboardService {

    @Autowired private AdminUsersRepository adminUsersRepository;
    @Autowired private AdminProductsRepository adminProductsRepository;
    @Autowired private AdminProposalsRepository adminProposalsRepository;
    @Autowired private AdminOrdersRepository adminOrdersRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDTO getDashboardStats() {
        // 1. Cálculos de Usuarios usando tu repositorio Admin
        long particulars = adminUsersRepository.countByRole(Rol.PARTICULAR);
        long companies = adminUsersRepository.countByRole(Rol.EMPRESA);

        // 2. Cálculos de Productos 
        long activeProducts = adminProductsRepository.countByProductStatus(ProductStatus.ACTIVE);
        long hiddenProducts = adminProductsRepository.countByProductStatus(ProductStatus.HIDDEN);

        // 3. Ingresos (Suma de la tabla orders)
        Double totalRev = adminOrdersRepository.sumTotalAmountByStatus("COMPLETED");

        return AdminDashboardStatsDTO.builder()
            .totalUsers(particulars + companies)
            .totalProducts(activeProducts + hiddenProducts)
            .totalProposals(adminProposalsRepository.count())
            .totalOrders(adminOrdersRepository.count())
            .totalRevenue(totalRev != null ? totalRev : 0.0)
            
            .usersByRole(Map.of("Particulares", particulars, "Empresas", companies))
            .productsByStatus(Map.of("Activos", activeProducts, "Inactivos", hiddenProducts))
            .build();
    }
}