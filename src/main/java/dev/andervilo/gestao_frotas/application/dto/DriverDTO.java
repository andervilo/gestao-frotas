package dev.andervilo.gestao_frotas.application.dto;

import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Driver data transfer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    
    private UUID id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "CPF is required")
    private String cpf;
    
    @NotBlank(message = "CNH is required")
    private String cnh;
    
    @NotBlank(message = "CNH category is required")
    private String cnhCategory;
    
    @NotNull(message = "CNH expiration date is required")
    private LocalDate cnhExpirationDate;
    
    private DriverStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
