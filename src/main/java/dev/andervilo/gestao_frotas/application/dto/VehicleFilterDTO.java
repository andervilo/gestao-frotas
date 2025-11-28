package dev.andervilo.gestao_frotas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for filtering vehicles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleFilterDTO {
    
    private String licensePlate;
    private String brand;
    private String model;
    private Integer yearFrom;
    private Integer yearTo;
}
