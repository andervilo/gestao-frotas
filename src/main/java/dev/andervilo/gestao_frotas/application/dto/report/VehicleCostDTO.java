package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record VehicleCostDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    BigDecimal totalCost,
    BigDecimal maintenanceCost,
    Integer totalKm,
    BigDecimal costPerKm,
    Integer ranking
) {}
