package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.util.List;

public record DepreciationReportDTO(
    BigDecimal totalFleetValue,
    BigDecimal totalDepreciation,
    Integer averageFleetAge,
    List<VehicleDepreciationDTO> vehicleDepreciation
) {}
