package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record OverdueMaintenanceDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    String type,
    LocalDate scheduledDate,
    Integer daysOverdue,
    String severity
) {}
