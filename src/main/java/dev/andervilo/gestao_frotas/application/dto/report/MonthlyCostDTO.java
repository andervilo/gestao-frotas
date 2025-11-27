package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record MonthlyCostDTO(
    String month,
    BigDecimal totalCost,
    BigDecimal maintenanceCost,
    Integer totalKm
) {}
