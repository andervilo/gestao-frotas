package dev.andervilo.gestao_frotas.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Trip data transfer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    
    private UUID id;
    
    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;
    
    @NotNull(message = "Driver ID is required")
    private UUID driverId;
    
    @NotBlank(message = "Origin is required")
    private String origin;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    
    @NotNull(message = "Start mileage is required")
    @Min(value = 0, message = "Start mileage must be non-negative")
    private Long startMileage;
    
    private Long endMileage;
    private Long distanceTraveled;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
