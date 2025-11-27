package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record ExpiredDocumentDTO(
    String vehicleId,
    String licensePlate,
    String documentType,
    LocalDate expirationDate,
    Integer daysExpired,
    String severity
) {}
