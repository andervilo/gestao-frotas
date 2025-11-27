package dev.andervilo.gestao_frotas.application.dto;

import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Maintenance data transfer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDTO {
    
    private UUID id;
    
    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;
    
    @NotNull(message = "Maintenance type is required")
    private MaintenanceType type;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private BigDecimal cost;
    
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;
    
    private LocalDateTime startDate;
    private LocalDateTime completionDate;
    private MaintenanceStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
