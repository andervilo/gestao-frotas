package dev.andervilo.gestao_frotas.application.dto;

import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for filtering maintenances.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceFilterDTO {
    
    private UUID vehicleId;
    private MaintenanceType type;
    private MaintenanceStatus status;
    private String description;
    private LocalDate scheduledDateFrom;
    private LocalDate scheduledDateTo;
    private LocalDate completedDateFrom;
    private LocalDate completedDateTo;
}
