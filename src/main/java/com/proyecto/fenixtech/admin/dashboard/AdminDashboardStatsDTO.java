package com.proyecto.fenixtech.admin.dashboard;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class AdminDashboardStatsDTO {
    private Long totalUsers;
    private Long totalProducts;
    private Long totalProposals;
    private Long totalOrders;
    private Double totalRevenue;

    private Map<String, Long> usersByRole;      
    private Map<String, Long> productsByStatus; 
    private Map<String, Long> proposalsByStatus;
    private Map<String, Long> ordersByType;     

    private Map<String, Double> revenueByMonth; 
}