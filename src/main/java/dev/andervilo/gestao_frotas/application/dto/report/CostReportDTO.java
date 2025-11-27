package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CostReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal totalCost,
    BigDecimal totalMaintenance,
    BigDecimal averageCostPerVehicle,
    BigDecimal averageCostPerKm,
    List<VehicleCostDTO> vehicleCosts,
    List<MonthlyCostDTO> monthlyCosts,
    List<CostProjectionDTO> projections
) {}
