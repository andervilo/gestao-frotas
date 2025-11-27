package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenerateFleetUtilizationReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateFleetUtilizationReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public FleetUtilizationReportDTO execute(LocalDate startDate, LocalDate endDate) {
        // Buscar dados de utilização
        List<Map<String, Object>> utilizationRaw = reportRepository.getVehicleUtilization(startDate, endDate);
        List<VehicleUtilizationDTO> vehicleUtilization = mapToVehicleUtilization(utilizationRaw);
        
        // Buscar veículos ociosos (mais de 30 dias parados)
        List<Map<String, Object>> idleRaw = reportRepository.getIdleVehicles(30);
        List<IdleVehicleDTO> idleVehicles = mapToIdleVehicles(idleRaw);
        
        // Calcular estatísticas
        BigDecimal avgUtilization = vehicleUtilization.isEmpty() 
            ? BigDecimal.ZERO
            : vehicleUtilization.stream()
                .map(VehicleUtilizationDTO::utilizationRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(vehicleUtilization.size()), 2, RoundingMode.HALF_UP);
        
        int activeVehicles = (int) vehicleUtilization.stream()
            .filter(v -> v.utilizationRate().compareTo(BigDecimal.valueOf(10)) > 0)
            .count();
        
        return new FleetUtilizationReportDTO(
            startDate,
            endDate,
            avgUtilization,
            vehicleUtilization.size(),
            activeVehicles,
            idleVehicles.size(),
            vehicleUtilization,
            idleVehicles
        );
    }
    
    private List<VehicleUtilizationDTO> mapToVehicleUtilization(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> {
                BigDecimal rate = new BigDecimal(row.get("utilizationRate").toString());
                return new VehicleUtilizationDTO(
                    (String) row.get("vehicleId"),
                    (String) row.get("licensePlate"),
                    (String) row.get("brand"),
                    (String) row.get("model"),
                    ((Number) row.get("totalTrips")).intValue(),
                    ((Number) row.get("totalKm")).intValue(),
                    ((Number) row.get("daysInUse")).intValue(),
                    ((Number) row.get("daysInMaintenance")).intValue(),
                    ((Number) row.get("daysIdle")).intValue(),
                    rate,
                    getUtilizationStatus(rate)
                );
            })
            .collect(Collectors.toList());
    }
    
    private String getUtilizationStatus(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.valueOf(70)) >= 0) return "ALTO";
        if (rate.compareTo(BigDecimal.valueOf(30)) >= 0) return "MEDIO";
        return "BAIXO";
    }
    
    private List<IdleVehicleDTO> mapToIdleVehicles(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> {
                int daysIdle = row.get("daysIdle") != null 
                    ? ((Number) row.get("daysIdle")).intValue() 
                    : 365;
                Object lastTripDateObj = row.get("lastTripDate");
                LocalDate lastTripDate = lastTripDateObj != null 
                    ? LocalDate.parse(lastTripDateObj.toString()) 
                    : null;
                return new IdleVehicleDTO(
                    (String) row.get("vehicleId"),
                    (String) row.get("licensePlate"),
                    (String) row.get("brand"),
                    (String) row.get("model"),
                    daysIdle,
                    lastTripDate,
                    getSuggestion(daysIdle)
                );
            })
            .collect(Collectors.toList());
    }
    
    private String getSuggestion(int daysIdle) {
        if (daysIdle > 180) return "VENDER";
        if (daysIdle > 90) return "LOCAR";
        return "MANTER";
    }
}
