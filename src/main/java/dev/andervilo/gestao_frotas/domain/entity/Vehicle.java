package dev.andervilo.gestao_frotas.domain.entity;

import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a vehicle in the fleet.
 * This is a rich domain model with business logic and validation.
 */
@Getter
@Builder
@AllArgsConstructor
public class Vehicle {
    
    private UUID id;
    private LicensePlate licensePlate;
    private VehicleType type;
    private String brand;
    private String model;
    private Integer year;
    private VehicleStatus status;
    private Long currentMileage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Creates a new vehicle with validation.
     */
    public static Vehicle create(
            LicensePlate licensePlate,
            VehicleType type,
            String brand,
            String model,
            Integer year,
            Long initialMileage) {
        
        validate(licensePlate, type, brand, model, year, initialMileage);
        
        return Vehicle.builder()
                .id(UUID.randomUUID())
                .licensePlate(licensePlate)
                .type(type)
                .brand(brand)
                .model(model)
                .year(year)
                .status(VehicleStatus.AVAILABLE)
                .currentMileage(initialMileage)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Updates vehicle information.
     */
    public void update(String brand, String model, Integer year) {
        if (brand != null && !brand.isBlank()) {
            this.brand = brand;
        }
        if (model != null && !model.isBlank()) {
            this.model = model;
        }
        if (year != null && year > 1900) {
            this.year = year;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates the vehicle's mileage.
     */
    public void updateMileage(Long newMileage) {
        if (newMileage == null || newMileage < this.currentMileage) {
            throw new IllegalArgumentException("New mileage must be greater than current mileage");
        }
        this.currentMileage = newMileage;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Changes the vehicle status.
     */
    public void changeStatus(VehicleStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Marks vehicle as in use.
     */
    public void markAsInUse() {
        if (this.status != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("Vehicle must be available to be marked as in use");
        }
        changeStatus(VehicleStatus.IN_USE);
    }
    
    /**
     * Marks vehicle as available.
     */
    public void markAsAvailable() {
        changeStatus(VehicleStatus.AVAILABLE);
    }
    
    /**
     * Marks vehicle as in maintenance.
     */
    public void markAsInMaintenance() {
        changeStatus(VehicleStatus.MAINTENANCE);
    }
    
    /**
     * Checks if vehicle is available for use.
     */
    public boolean isAvailable() {
        return this.status == VehicleStatus.AVAILABLE;
    }
    
    private static void validate(
            LicensePlate licensePlate,
            VehicleType type,
            String brand,
            String model,
            Integer year,
            Long initialMileage) {
        
        if (licensePlate == null) {
            throw new IllegalArgumentException("License plate cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (year == null || year < 1900 || year > LocalDateTime.now().getYear() + 1) {
            throw new IllegalArgumentException("Invalid year");
        }
        if (initialMileage == null || initialMileage < 0) {
            throw new IllegalArgumentException("Initial mileage must be non-negative");
        }
    }
}
