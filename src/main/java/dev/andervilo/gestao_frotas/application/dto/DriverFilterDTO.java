package dev.andervilo.gestao_frotas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for filtering drivers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverFilterDTO {
    
    private String name;
    private String cpf;
    private String cnh;
    private String cnhCategory;
    private LocalDate cnhExpirationDateFrom;
    private LocalDate cnhExpirationDateTo;
}
