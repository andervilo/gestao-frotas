package dev.andervilo.gestao_frotas.application.dto.report;

public record RouteFrequencyDTO(
    String origin,
    String destination,
    Integer tripCount,
    Integer totalKm,
    Integer averageKm
) {}
