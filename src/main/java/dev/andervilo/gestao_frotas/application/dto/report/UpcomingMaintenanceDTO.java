package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record UpcomingMaintenanceDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    String type,
    LocalDate scheduledDate,
    Integer daysUntil,
    String priority
) {}
