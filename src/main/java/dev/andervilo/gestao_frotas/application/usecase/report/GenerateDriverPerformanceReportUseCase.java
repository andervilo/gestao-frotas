package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GenerateDriverPerformanceReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateDriverPerformanceReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public DriverPerformanceReportDTO execute(LocalDate startDate, LocalDate endDate) {
        List<java.util.Map<String, Object>> driverPerformance = reportRepository.getDriverPerformance(startDate, endDate);
        List<java.util.Map<String, Object>> cnhExpiring = reportRepository.getDriversWithCNHExpiringSoon(90);
        
        List<DriverStatsDTO> stats = mapToDriverStats(driverPerformance);
        List<DriverCNHExpiringDTO> expiring = mapToCNHExpiring(cnhExpiring);
        
        // Calculate totals
        int totalDrivers = stats.size();
        int activeDrivers = (int) stats.stream().filter(s -> s.totalTrips() > 0).count();
        
        return new DriverPerformanceReportDTO(
            startDate,
            endDate,
            totalDrivers,
            activeDrivers,
            stats,
            expiring
        );
    }
    
    private List<DriverStatsDTO> mapToDriverStats(List<java.util.Map<String, Object>> data) {
        return data.stream().map(row -> {
            String driverId = row.get("driverid") != null ? row.get("driverid").toString() : null;
            String name = row.get("name") != null ? row.get("name").toString() : null;
            String cnh = row.get("cnh") != null ? row.get("cnh").toString() : null;
            Integer totalTrips = row.get("totaltrips") != null ? ((Number) row.get("totaltrips")).intValue() : 0;
            Integer totalKm = row.get("totalkm") != null ? ((Number) row.get("totalkm")).intValue() : 0;
            BigDecimal utilizationRate = parseBigDecimal(row.get("utilizationrate"));
            
            return new DriverStatsDTO(
                driverId,
                name,
                cnh,
                totalTrips,
                totalKm,
                utilizationRate,
                0 // ranking será calculado posteriormente se necessário
            );
        }).toList();
    }
    
    private List<DriverCNHExpiringDTO> mapToCNHExpiring(List<java.util.Map<String, Object>> data) {
        return data.stream().map(row -> {
            String driverId = row.get("driverid") != null ? row.get("driverid").toString() : null;
            String name = row.get("name") != null ? row.get("name").toString() : null;
            String cnh = row.get("cnh") != null ? row.get("cnh").toString() : null;
            LocalDate cnhExpiration = parseDate(row.get("cnhexpiration"));
            
            int daysUntilExpiration = row.get("daysuntilexpiration") != null 
                ? ((Number) row.get("daysuntilexpiration")).intValue()
                : (cnhExpiration != null ? (int) ChronoUnit.DAYS.between(LocalDate.now(), cnhExpiration) : 0);
            
            String status = getExpirationStatus(daysUntilExpiration);
            
            return new DriverCNHExpiringDTO(
                driverId,
                name,
                cnh,
                cnhExpiration,
                daysUntilExpiration,
                status
            );
        }).toList();
    }
    
    private String getExpirationStatus(int daysUntil) {
        if (daysUntil <= 0) {
            return "VENCIDA";
        } else if (daysUntil <= 30) {
            return "URGENTE";
        } else if (daysUntil <= 60) {
            return "ALTA";
        } else if (daysUntil <= 90) {
            return "MÉDIA";
        } else {
            return "BAIXA";
        }
    }
    
    private LocalDate parseDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        if (obj instanceof LocalDateTime) {
            return ((LocalDateTime) obj).toLocalDate();
        }
        if (obj instanceof java.sql.Date) {
            return ((java.sql.Date) obj).toLocalDate();
        }
        if (obj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) obj).toLocalDateTime().toLocalDate();
        }
        if (obj instanceof String) {
            try {
                return LocalDate.parse((String) obj, DateTimeFormatter.ISO_DATE);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    private BigDecimal parseBigDecimal(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return new BigDecimal(obj.toString());
        }
        if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj);
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}
