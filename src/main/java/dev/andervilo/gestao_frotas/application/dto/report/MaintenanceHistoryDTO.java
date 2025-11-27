package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceHistoryDTO(
    String vehicleId,
    String licensePlate,
    String type,
    String description,
    LocalDate scheduledDate,
    LocalDate completionDate,
    BigDecimal cost,
    String status
) {}
