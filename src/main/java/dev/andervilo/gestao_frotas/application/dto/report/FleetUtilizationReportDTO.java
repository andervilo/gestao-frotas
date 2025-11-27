package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FleetUtilizationReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal averageUtilizationRate,
    Integer totalVehicles,
    Integer activeVehicles,
    Integer idleVehicles,
    List<VehicleUtilizationDTO> vehicleUtilization,
    List<IdleVehicleDTO> idleVehiclesList
) {}
