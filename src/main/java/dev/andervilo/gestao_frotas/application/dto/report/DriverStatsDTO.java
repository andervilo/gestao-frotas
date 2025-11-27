package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record DriverStatsDTO(
    String driverId,
    String name,
    String cnh,
    Integer totalTrips,
    Integer totalKm,
    BigDecimal utilizationRate,
    Integer ranking
) {}
