package dev.andervilo.gestao_frotas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for dashboard statistics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    
    private Long totalVehicles;
    private Long availableVehicles;
    private Long inUseVehicles;
    private Long inMaintenanceVehicles;
    
    private Long totalDrivers;
    private Long activeDrivers;
    private Long inactiveDrivers;
    
    private Long totalTrips;
    private Long scheduledTrips;
    private Long inProgressTrips;
    private Long completedTrips;
    
    private Long totalMaintenances;
    private Long scheduledMaintenances;
    private Long inProgressMaintenances;
    private Long completedMaintenances;
}
