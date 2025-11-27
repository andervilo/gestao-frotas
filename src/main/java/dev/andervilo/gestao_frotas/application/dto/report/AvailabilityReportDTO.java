package dev.andervilo.gestao_frotas.application.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AvailabilityReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal averageAvailability,
    List<VehicleAvailabilityDTO> vehicleAvailability
) {}
