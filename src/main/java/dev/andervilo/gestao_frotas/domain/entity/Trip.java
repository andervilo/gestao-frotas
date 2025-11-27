package dev.andervilo.gestao_frotas.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a trip/journey made by a vehicle and driver.
 */
@Getter
@Builder
@AllArgsConstructor
public class Trip {
    
    private UUID id;
    private Vehicle vehicle;
    private Driver driver;
    private String origin;
    private String destination;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long startMileage;
    private Long endMileage;
    private Long distanceTraveled;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Creates a new trip.
     */
    public static Trip create(
            Vehicle vehicle,
            Driver driver,
            String origin,
            String destination,
            Long startMileage) {
        
        validate(vehicle, driver, origin, destination, startMileage);
        
        return Trip.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .driver(driver)
                .origin(origin)
                .destination(destination)
                .startDateTime(LocalDateTime.now())
                .startMileage(startMileage)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Completes the trip.
     */
    public void complete(Long endMileage, String notes) {
        if (this.endDateTime != null) {
            throw new IllegalStateException("Trip is already completed");
        }
        if (endMileage == null || endMileage <= this.startMileage) {
            throw new IllegalArgumentException("End mileage must be greater than start mileage");
        }
        
        this.endDateTime = LocalDateTime.now();
        this.endMileage = endMileage;
        this.distanceTraveled = endMileage - this.startMileage;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates trip notes.
     */
    public void updateNotes(String notes) {
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Checks if trip is completed.
     */
    public boolean isCompleted() {
        return this.endDateTime != null;
    }
    
    /**
     * Checks if trip is in progress.
     */
    public boolean isInProgress() {
        return this.endDateTime == null;
    }
    
    /**
     * Gets trip duration in minutes.
     */
    public Long getDurationInMinutes() {
        if (!isCompleted()) {
            return null;
        }
        return java.time.Duration.between(startDateTime, endDateTime).toMinutes();
    }
    
    private static void validate(
            Vehicle vehicle,
            Driver driver,
            String origin,
            String destination,
            Long startMileage) {
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin cannot be null or empty");
        }
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (startMileage == null || startMileage < 0) {
            throw new IllegalArgumentException("Start mileage must be non-negative");
        }
    }
}
