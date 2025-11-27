package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record MaintenanceReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    Integer totalMaintenances,
    Integer preventiveCount,
    Integer correctiveCount,
    BigDecimal totalCost,
    BigDecimal averageDaysInMaintenance,
    List<MaintenanceHistoryDTO> history,
    List<UpcomingMaintenanceDTO> upcoming,
    List<OverdueMaintenanceDTO> overdue
) {}
