package dev.andervilo.gestao_frotas.application.dto;

import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Vehicle data transfer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    
    private UUID id;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType type;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be after 1900")
    private Integer year;
    
    private VehicleStatus status;
    
    @NotNull(message = "Current mileage is required")
    @Min(value = 0, message = "Mileage must be non-negative")
    private Long currentMileage;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
