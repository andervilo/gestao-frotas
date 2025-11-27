package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GenerateDepreciationReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateDepreciationReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public DepreciationReportDTO execute() {
        List<java.util.Map<String, Object>> vehicleDepreciation = reportRepository.getVehicleDepreciation();
        
        List<VehicleDepreciationDTO> depreciations = mapToVehicleDepreciation(vehicleDepreciation);
        
        // Calculate totals
        BigDecimal totalFleetValue = depreciations.stream()
            .map(VehicleDepreciationDTO::currentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalDepreciation = depreciations.stream()
            .map(VehicleDepreciationDTO::totalDepreciation)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int avgFleetAge = depreciations.isEmpty() ? 0 :
            (int) depreciations.stream()
                .mapToInt(VehicleDepreciationDTO::ageInYears)
                .average()
                .orElse(0);
        
        return new DepreciationReportDTO(
            totalFleetValue,
            totalDepreciation,
            avgFleetAge,
            depreciations
        );
    }
    
    private List<VehicleDepreciationDTO> mapToVehicleDepreciation(List<java.util.Map<String, Object>> data) {
        return data.stream().map(row -> {
            String vehicleId = row.get("vehicleId").toString();
            String licensePlate = (String) row.get("licensePlate");
            String brand = (String) row.get("brand");
            String model = (String) row.get("model");
            Integer year = row.get("year") != null ? ((Number) row.get("year")).intValue() : 0;
            BigDecimal purchaseValue = parseBigDecimal(row.get("purchaseValue"));
            BigDecimal currentValue = parseBigDecimal(row.get("currentValue"));
            BigDecimal totalDepreciation = parseBigDecimal(row.get("totalDepreciation"));
            BigDecimal depreciationRate = parseBigDecimal(row.get("depreciationRate"));
            Integer ageInYears = row.get("ageInYears") != null ? ((Number) row.get("ageInYears")).intValue() : 0;
            LocalDate purchaseDate = parseDate(row.get("purchaseDate"));
            
            return new VehicleDepreciationDTO(
                vehicleId,
                licensePlate,
                brand,
                model,
                year,
                purchaseValue,
                currentValue,
                totalDepreciation,
                depreciationRate,
                ageInYears,
                purchaseDate
            );
        }).toList();
    }
    
    private LocalDate parseDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        if (obj instanceof java.sql.Date) {
            return ((java.sql.Date) obj).toLocalDate();
        }
        if (obj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) obj).toLocalDateTime().toLocalDate();
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
        return BigDecimal.ZERO;
    }
}
