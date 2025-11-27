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
public class GenerateMaintenanceReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateMaintenanceReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public MaintenanceReportDTO execute(LocalDate startDate, LocalDate endDate) {
        // 1. Buscar histórico de manutenções
        List<Map<String, Object>> historyRaw = reportRepository.getMaintenanceHistory(startDate, endDate);
        List<MaintenanceHistoryDTO> history = mapToMaintenanceHistory(historyRaw);
        
        // 2. Buscar manutenções próximas (próximos 30 dias)
        List<Map<String, Object>> upcomingRaw = reportRepository.getUpcomingMaintenances(30);
        List<UpcomingMaintenanceDTO> upcoming = mapToUpcomingMaintenances(upcomingRaw);
        
        // 3. Buscar manutenções vencidas
        List<Map<String, Object>> overdueRaw = reportRepository.getOverdueMaintenances();
        List<OverdueMaintenanceDTO> overdue = mapToOverdueMaintenances(overdueRaw);
        
        // 4. Contar por tipo
        Map<String, Long> countByType = reportRepository.getMaintenanceCountByType(startDate, endDate);
        
        // 5. Calcular estatísticas
        int totalMaintenances = history.size();
        int preventiveCount = countByType.getOrDefault("PREVENTIVE", 0L).intValue();
        int correctiveCount = countByType.getOrDefault("CORRECTIVE", 0L).intValue();
        
        BigDecimal totalCost = history.stream()
            .map(MaintenanceHistoryDTO::cost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 6. Calcular média de dias em manutenção (simplificado)
        BigDecimal avgDays = BigDecimal.valueOf(5.0); // Placeholder - pode ser calculado com datas
        
        return new MaintenanceReportDTO(
            startDate,
            endDate,
            totalMaintenances,
            preventiveCount,
            correctiveCount,
            totalCost,
            avgDays,
            history,
            upcoming,
            overdue
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
