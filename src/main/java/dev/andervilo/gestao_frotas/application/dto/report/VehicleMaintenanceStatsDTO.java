package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record VehicleMaintenanceStatsDTO(
    String licensePlate,
    Integer maintenanceCount,
    BigDecimal totalCost
) {
}
