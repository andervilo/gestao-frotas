package dev.andervilo.gestao_frotas.application.dto.report;

import java.util.List;

public record DocumentationReportDTO(
    Integer totalVehicles,
    Integer vehiclesWithExpiredDocs,
    Integer vehiclesWithDocsExpiringSoon,
    List<ExpiredDocumentDTO> expiredDocuments,
    List<ExpiringDocumentDTO> expiringDocuments
) {}
