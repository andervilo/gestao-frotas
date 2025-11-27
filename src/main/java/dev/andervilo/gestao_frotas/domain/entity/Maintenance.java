package dev.andervilo.gestao_frotas.domain.entity;

import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a maintenance record for a vehicle.
 */
@Getter
@Builder
@AllArgsConstructor
public class Maintenance {
    
    private UUID id;
    private Vehicle vehicle;
    private MaintenanceType type;
    private String description;
    private BigDecimal cost;
    private LocalDateTime scheduledDate;
    private LocalDateTime startDate;
    private LocalDateTime completionDate;
    private MaintenanceStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Creates a new maintenance record.
     */
    public static Maintenance create(
            Vehicle vehicle,
            MaintenanceType type,
            String description,
            LocalDateTime scheduledDate) {
        
        validate(vehicle, type, description, scheduledDate);
        
        return Maintenance.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .type(type)
                .description(description)
                .scheduledDate(scheduledDate)
                .status(MaintenanceStatus.SCHEDULED)
                .cost(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Starts the maintenance.
     */
    public void start() {
        if (status != MaintenanceStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled maintenance can be started");
        }
        this.status = MaintenanceStatus.IN_PROGRESS;
        this.startDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Completes the maintenance.
     */
    public void complete(BigDecimal finalCost, String notes) {
        if (status != MaintenanceStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress maintenance can be completed");
        }
        if (finalCost != null && finalCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        this.status = MaintenanceStatus.COMPLETED;
        this.completionDate = LocalDateTime.now();
        this.cost = finalCost != null ? finalCost : this.cost;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancels the maintenance.
     */
    public void cancel(String reason) {
        if (status == MaintenanceStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed maintenance");
        }
        this.status = MaintenanceStatus.CANCELLED;
        this.notes = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates maintenance cost.
     */
    public void updateCost(BigDecimal newCost) {
        if (newCost == null || newCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost must be non-negative");
        }
        this.cost = newCost;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates maintenance description and notes.
     */
    public void updateDetails(String description, String notes) {
        if (description != null && !description.isBlank()) {
            this.description = description;
        }
        if (notes != null) {
            this.notes = notes;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Checks if maintenance is overdue.
     */
    public boolean isOverdue() {
        return status == MaintenanceStatus.SCHEDULED && 
               scheduledDate.isBefore(LocalDateTime.now());
    }
    
    private static void validate(
            Vehicle vehicle,
            MaintenanceType type,
            String description,
            LocalDateTime scheduledDate) {
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Maintenance type cannot be null");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
    }
}
