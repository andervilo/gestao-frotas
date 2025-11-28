package dev.andervilo.gestao_frotas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for filtering trips.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripFilterDTO {
    
    private UUID vehicleId;
    private UUID driverId;
    private String destination;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private LocalDateTime endDateFrom;
    private LocalDateTime endDateTo;
}
