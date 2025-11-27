package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record VehicleUtilizationDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    Integer totalTrips,
    Integer totalKm,
    Integer daysInUse,
    Integer daysInMaintenance,
    Integer daysIdle,
    BigDecimal utilizationRate,
    String utilizationStatus
) {}
