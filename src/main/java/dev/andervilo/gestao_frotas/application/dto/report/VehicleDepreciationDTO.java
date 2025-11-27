package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleDepreciationDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    Integer year,
    BigDecimal purchaseValue,
    BigDecimal currentValue,
    BigDecimal totalDepreciation,
    BigDecimal depreciationRate,
    Integer ageInYears,
    LocalDate purchaseDate
) {}
