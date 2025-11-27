package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record DriverCNHExpiringDTO(
    String driverId,
    String name,
    String cnh,
    LocalDate cnhExpiration,
    Integer daysUntilExpiration,
    String status
) {}
