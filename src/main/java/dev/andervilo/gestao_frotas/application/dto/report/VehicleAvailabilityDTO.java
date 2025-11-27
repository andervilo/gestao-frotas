package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;

public record VehicleAvailabilityDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    Integer daysAvailable,
    Integer daysInUse,
    Integer daysInMaintenance,
    BigDecimal availabilityRate,
    String availabilityStatus
) {}
