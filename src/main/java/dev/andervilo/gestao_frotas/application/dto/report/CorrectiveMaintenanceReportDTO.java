package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CorrectiveMaintenanceReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    Integer totalCorrectiveMaintenances,
    BigDecimal totalCost,
    BigDecimal averageCost,
    List<MaintenanceHistoryDTO> correctiveHistory,
    List<UpcomingMaintenanceDTO> upcomingCorrective,
    List<OverdueMaintenanceDTO> overdueCorrective,
    List<VehicleMaintenanceStatsDTO> topVehicles
) {
}
