package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record IdleVehicleDTO(
    String vehicleId,
    String licensePlate,
    String brand,
    String model,
    Integer daysIdle,
    LocalDate lastTripDate,
    String suggestion
) {}
