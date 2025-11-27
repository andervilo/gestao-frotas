package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;
import java.util.List;

public record DriverPerformanceReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    Integer totalDrivers,
    Integer activeDrivers,
    List<DriverStatsDTO> driverStats,
    List<DriverCNHExpiringDTO> cnhExpiring
) {}
