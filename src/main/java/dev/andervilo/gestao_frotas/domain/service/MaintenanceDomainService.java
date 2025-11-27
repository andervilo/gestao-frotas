package dev.andervilo.gestao_frotas.domain.service;

import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import dev.andervilo.gestao_frotas.domain.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Domain service for Maintenance aggregate.
 */
@Service
@RequiredArgsConstructor
public class MaintenanceDomainService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleDomainService vehicleDomainService;
    
    /**
     * Creates a new maintenance record.
     */
    public Maintenance createMaintenance(
            UUID vehicleId,
            MaintenanceType type,
            String description,
            LocalDateTime scheduledDate) {
        
        // Load vehicle to ensure it exists and to create proper relationship
        Vehicle vehicle = vehicleDomainService.findVehicleById(vehicleId);
        
        // Business rule: Mark vehicle as in maintenance when scheduling
        if (type == MaintenanceType.CORRECTIVE) {
            vehicleDomainService.markVehicleAsInMaintenance(vehicleId);
        }
        
        // Create domain entity
        Maintenance maintenance = Maintenance.create(vehicle, type, description, scheduledDate);
        
        return maintenanceRepository.save(maintenance);
    }
    
    /**
     * Starts a maintenance.
     */
    public Maintenance startMaintenance(UUID id) {
        Maintenance maintenance = findMaintenanceById(id);
        maintenance.start();
        
        // Mark vehicle as in maintenance
        vehicleDomainService.markVehicleAsInMaintenance(maintenance.getVehicle().getId());
        
        return maintenanceRepository.save(maintenance);
    }
    
    /**
     * Completes a maintenance.
     */
    public Maintenance completeMaintenance(UUID id, BigDecimal finalCost, String notes) {
        Maintenance maintenance = findMaintenanceById(id);
        maintenance.complete(finalCost, notes);
        
        // Mark vehicle as available after maintenance
        vehicleDomainService.markVehicleAsAvailable(maintenance.getVehicle().getId());
        
        return maintenanceRepository.save(maintenance);
    }
    
    /**
     * Cancels a maintenance.
     */
    public Maintenance cancelMaintenance(UUID id, String reason) {
        Maintenance maintenance = findMaintenanceById(id);
        maintenance.cancel(reason);
        
        // Mark vehicle as available if maintenance is cancelled
        vehicleDomainService.markVehicleAsAvailable(maintenance.getVehicle().getId());
        
        return maintenanceRepository.save(maintenance);
    }
    
    /**
     * Finds maintenance by ID.
     */
    public Maintenance findMaintenanceById(UUID id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance not found with id: " + id));
    }
    
    /**
     * Finds all maintenances.
     */
    public List<Maintenance> findAllMaintenances() {
        return maintenanceRepository.findAll();
    }
    
    /**
     * Finds maintenances by vehicle.
     */
    public List<Maintenance> findMaintenancesByVehicle(UUID vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Finds maintenances by status.
     */
    public List<Maintenance> findMaintenancesByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status);
    }
    
    /**
     * Finds maintenances by type.
     */
    public List<Maintenance> findMaintenancesByType(MaintenanceType type) {
        return maintenanceRepository.findByType(type);
    }
    
    /**
     * Deletes a maintenance.
     */
    public void deleteMaintenance(UUID id) {
        if (!maintenanceRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Maintenance not found with id: " + id);
        }
        maintenanceRepository.deleteById(id);
    }
}
