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
public class GenerateCostReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateCostReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public CostReportDTO execute(LocalDate startDate, LocalDate endDate) {
        // 1. Buscar custo total
        BigDecimal totalCost = reportRepository.getTotalMaintenanceCost(startDate, endDate);
        
        // 2. Buscar custos por veículo
        List<Map<String, Object>> vehicleCostsRaw = reportRepository.getVehicleCosts(startDate, endDate);
        List<VehicleCostDTO> vehicleCosts = mapToVehicleCosts(vehicleCostsRaw);
        
        // 3. Buscar custos mensais
        List<Map<String, Object>> monthlyCostsRaw = reportRepository.getMonthlyCosts(startDate, endDate);
        List<MonthlyCostDTO> monthlyCosts = mapToMonthlyCosts(monthlyCostsRaw);
        
        // 4. Calcular média por veículo
        BigDecimal avgCostPerVehicle = vehicleCosts.isEmpty() 
            ? BigDecimal.ZERO 
            : totalCost.divide(BigDecimal.valueOf(vehicleCosts.size()), 2, RoundingMode.HALF_UP);
        
        // 5. Calcular custo médio por km
        int totalKm = vehicleCosts.stream()
            .mapToInt(VehicleCostDTO::totalKm)
            .sum();
        BigDecimal avgCostPerKm = totalKm > 0 
            ? totalCost.divide(BigDecimal.valueOf(totalKm), 4, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        // 6. Gerar projeções (simples: média dos últimos 3 meses)
        List<CostProjectionDTO> projections = generateProjections(monthlyCosts);
        
        return new CostReportDTO(
            startDate,
            endDate,
            totalCost,
            totalCost,
            avgCostPerVehicle,
            avgCostPerKm,
            vehicleCosts,
            monthlyCosts,
            projections
        );
    }
    
    private List<VehicleCostDTO> mapToVehicleCosts(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> new VehicleCostDTO(
                (String) row.get("vehicleId"),
                (String) row.get("licensePlate"),
                (String) row.get("brand"),
                (String) row.get("model"),
                new BigDecimal(row.get("totalCost").toString()),
                new BigDecimal(row.get("maintenanceCost").toString()),
                ((Number) row.get("totalKm")).intValue(),
                new BigDecimal(row.get("costPerKm").toString()),
                0
            ))
            .collect(Collectors.toList());
    }
    
    private List<MonthlyCostDTO> mapToMonthlyCosts(List<Map<String, Object>> raw) {
        return raw.stream()
            .map(row -> new MonthlyCostDTO(
                (String) row.get("month"),
                new BigDecimal(row.get("totalCost").toString()),
                new BigDecimal(row.get("maintenanceCost").toString()),
                ((Number) row.get("totalKm")).intValue()
            ))
            .collect(Collectors.toList());
    }
    
    private List<CostProjectionDTO> generateProjections(List<MonthlyCostDTO> history) {
        if (history.size() < 3) return List.of();
        
        BigDecimal avg = history.stream()
            .limit(3)
            .map(MonthlyCostDTO::totalCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
        
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return List.of(
            new CostProjectionDTO(nextMonth.toString().substring(0, 7), avg, BigDecimal.ZERO),
            new CostProjectionDTO(nextMonth.plusMonths(1).toString().substring(0, 7), avg, BigDecimal.ZERO),
            new CostProjectionDTO(nextMonth.plusMonths(2).toString().substring(0, 7), avg, BigDecimal.ZERO)
        );
    }
}
