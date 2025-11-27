package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record TripReportDTO(
    LocalDate startDate,
    LocalDate endDate,
    Integer totalTrips,
    Integer totalKm,
    Double averageDistance,
    List<RouteFrequencyDTO> topRoutes,
    Map<String, Integer> tripsByStatus
) {}
