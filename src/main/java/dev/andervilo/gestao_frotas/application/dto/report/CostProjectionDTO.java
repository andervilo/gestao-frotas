package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record CostProjectionDTO(
    String month,
    BigDecimal projectedCost,
    BigDecimal trend
) {}
