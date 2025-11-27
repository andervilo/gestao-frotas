package dev.andervilo.gestao_frotas.application.dto.report;

import java.time.LocalDate;

public record ExpiringDocumentDTO(
    String vehicleId,
    String licensePlate,
    String documentType,
    LocalDate expirationDate,
    Integer daysUntilExpiration,
    String priority
) {}
