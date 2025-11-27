package dev.andervilo.gestao_frotas.application.usecase.dashboard;

import dev.andervilo.gestao_frotas.application.dto.DashboardStatsDTO;
import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.repository.DriverRepository;
import dev.andervilo.gestao_frotas.domain.repository.MaintenanceRepository;
import dev.andervilo.gestao_frotas.domain.repository.TripRepository;
import dev.andervilo.gestao_frotas.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for retrieving dashboard statistics.
 */
@Service
@RequiredArgsConstructor
public class GetDashboardStatsUseCase {
    
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final MaintenanceRepository maintenanceRepository;
    
    @Transactional(readOnly = true)
    public DashboardStatsDTO execute() {
        return DashboardStatsDTO.builder()
                // Vehicle stats
                .totalVehicles((long) vehicleRepository.findAll().size())
                .availableVehicles((long) vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).size())
                .inUseVehicles((long) vehicleRepository.findByStatus(VehicleStatus.IN_USE).size())
                .inMaintenanceVehicles((long) vehicleRepository.findByStatus(VehicleStatus.MAINTENANCE).size())
                
                // Driver stats
                .totalDrivers((long) driverRepository.findAll().size())
                .activeDrivers((long) driverRepository.findByStatus(DriverStatus.ACTIVE).size())
                .inactiveDrivers((long) driverRepository.findByStatus(DriverStatus.INACTIVE).size())
                
                // Trip stats
                .totalTrips((long) tripRepository.findAll().size())
                .scheduledTrips((long) tripRepository.findScheduledTrips().size())
                .inProgressTrips((long) tripRepository.findInProgressTrips().size())
                .completedTrips((long) tripRepository.findCompletedTrips().size())
                
                // Maintenance stats
                .totalMaintenances((long) maintenanceRepository.findAll().size())
                .scheduledMaintenances((long) maintenanceRepository.findByStatus(MaintenanceStatus.SCHEDULED).size())
                .inProgressMaintenances((long) maintenanceRepository.findByStatus(MaintenanceStatus.IN_PROGRESS).size())
                .completedMaintenances((long) maintenanceRepository.findByStatus(MaintenanceStatus.COMPLETED).size())
                
                .build();
    }
}
