package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenerateCorrectiveMaintenanceReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateCorrectiveMaintenanceReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public CorrectiveMaintenanceReportDTO execute(LocalDate startDate, LocalDate endDate) {
        // 1. Buscar histórico de manutenções corretivas
        List<Map<String, Object>> historyRaw = reportRepository.getMaintenanceHistory(startDate, endDate);
        List<MaintenanceHistoryDTO> allHistory = mapToMaintenanceHistory(historyRaw);
        
        // Filtrar apenas corretivas
        List<MaintenanceHistoryDTO> correctiveHistory = allHistory.stream()
            .filter(m -> "CORRECTIVE".equalsIgnoreCase(m.type()) || "Corretiva".equalsIgnoreCase(m.type()))
            .collect(Collectors.toList());
        
        // 2. Buscar manutenções próximas (próximos 30 dias) - apenas corretivas agendadas
        List<Map<String, Object>> upcomingRaw = reportRepository.getUpcomingMaintenances(30);
        List<UpcomingMaintenanceDTO> upcomingCorrective = mapToUpcomingMaintenances(upcomingRaw).stream()
            .filter(m -> "CORRECTIVE".equalsIgnoreCase(m.type()) || "Corretiva".equalsIgnoreCase(m.type()))
            .collect(Collectors.toList());
        
        // 3. Buscar manutenções vencidas - apenas corretivas
        List<Map<String, Object>> overdueRaw = reportRepository.getOverdueMaintenances();
        List<OverdueMaintenanceDTO> overdueCorrective = mapToOverdueMaintenances(overdueRaw).stream()
            .filter(m -> "CORRECTIVE".equalsIgnoreCase(m.type()) || "Corretiva".equalsIgnoreCase(m.type()))
            .collect(Collectors.toList());
        
        // 4. Calcular estatísticas
        int totalCorrectiveMaintenances = correctiveHistory.size();
        
        BigDecimal totalCost = correctiveHistory.stream()
            .map(MaintenanceHistoryDTO::cost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal avgCost = totalCorrectiveMaintenances > 0 
            ? totalCost.divide(BigDecimal.valueOf(totalCorrectiveMaintenances), 2, BigDecimal.ROUND_HALF_UP)
            : BigDecimal.ZERO;
        
        // 5. Calcular veículos com mais manutenções corretivas
        Map<String, Long> vehicleMaintenanceCount = correctiveHistory.stream()
            .collect(Collectors.groupingBy(
                m -> m.licensePlate() + " - " + m.vehicleId(),
                Collectors.counting()
            ));
        
        List<VehicleMaintenanceStatsDTO> topVehicles = vehicleMaintenanceCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(10)
            .map(entry -> {
                String[] parts = entry.getKey().split(" - ");
                String licensePlate = parts.length > 0 ? parts[0] : "";
                
                BigDecimal vehicleCost = correctiveHistory.stream()
                    .filter(m -> m.licensePlate().equals(licensePlate))
                    .map(MaintenanceHistoryDTO::cost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                return new VehicleMaintenanceStatsDTO(
                    licensePlate,
                    entry.getValue().intValue(),
                    vehicleCost
                );
            })
            .collect(Collectors.toList());
        
        return new CorrectiveMaintenanceReportDTO(
            startDate,
            endDate,
            totalCorrectiveMaintenances,
            totalCost,
            avgCost,
            correctiveHistory,
            upcomingCorrective,
            overdueCorrective,
            topVehicles
        );
    }
    
    private List<MaintenanceHistoryDTO> mapToMaintenanceHistory(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> new MaintenanceHistoryDTO(
                row.get("vehicleid") != null ? row.get("vehicleid").toString() : null,
                row.get("licenseplate") != null ? row.get("licenseplate").toString() : null,
                row.get("type") != null ? row.get("type").toString() : null,
                row.get("description") != null ? row.get("description").toString() : null,
                parseDate(row.get("scheduleddate")),
                parseDate(row.get("completiondate")),
                parseBigDecimal(row.get("cost")),
                row.get("status") != null ? row.get("status").toString() : null
            ))
            .collect(Collectors.toList());
    }
    
    private List<UpcomingMaintenanceDTO> mapToUpcomingMaintenances(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> {
                LocalDate scheduledDate = parseDate(row.get("scheduleddate"));
                int daysUntil = row.get("daysuntil") != null 
                    ? ((Number) row.get("daysuntil")).intValue()
                    : (scheduledDate != null ? (int) ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate) : 0);
                
                return new UpcomingMaintenanceDTO(
                    row.get("vehicleid") != null ? row.get("vehicleid").toString() : null,
                    row.get("licenseplate") != null ? row.get("licenseplate").toString() : null,
                    row.get("brand") != null ? row.get("brand").toString() : null,
                    row.get("model") != null ? row.get("model").toString() : null,
                    row.get("type") != null ? row.get("type").toString() : null,
                    scheduledDate,
                    daysUntil,
                    getPriority(daysUntil)
                );
            })
            .collect(Collectors.toList());
    }
    
    private List<OverdueMaintenanceDTO> mapToOverdueMaintenances(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> {
                int daysOverdue = row.get("daysoverdue") != null 
                    ? ((Number) row.get("daysoverdue")).intValue()
                    : 0;
                
                return new OverdueMaintenanceDTO(
                    row.get("vehicleid") != null ? row.get("vehicleid").toString() : null,
                    row.get("licenseplate") != null ? row.get("licenseplate").toString() : null,
                    row.get("brand") != null ? row.get("brand").toString() : null,
                    row.get("model") != null ? row.get("model").toString() : null,
                    row.get("type") != null ? row.get("type").toString() : null,
                    parseDate(row.get("scheduleddate")),
                    daysOverdue,
                    getSeverity(daysOverdue)
                );
            })
            .collect(Collectors.toList());
    }
    
    private String getPriority(int daysUntil) {
        if (daysUntil <= 7) return "ALTA";
        if (daysUntil <= 15) return "MEDIA";
        return "BAIXA";
    }
    
    private String getSeverity(int daysOverdue) {
        if (daysOverdue > 30) return "CRITICA";
        if (daysOverdue > 15) return "ALTA";
        return "MEDIA";
    }
    
    private LocalDate parseDate(Object dateObj) {
        if (dateObj == null) return null;
        if (dateObj instanceof LocalDate) return (LocalDate) dateObj;
        return LocalDate.parse(dateObj.toString());
    }
    
    private BigDecimal parseBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        return new BigDecimal(value.toString());
    }
}
