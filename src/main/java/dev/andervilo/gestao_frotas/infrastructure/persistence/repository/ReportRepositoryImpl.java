package dev.andervilo.gestao_frotas.infrastructure.persistence.repository;

import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.ReportQueriesRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataReportRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportRepositoryImpl implements ReportRepository {
    
    private final SpringDataReportRepository springDataReportRepository;
    private final ReportQueriesRepository reportQueriesRepository;
    
    public ReportRepositoryImpl(
        SpringDataReportRepository springDataReportRepository,
        ReportQueriesRepository reportQueriesRepository
    ) {
        this.springDataReportRepository = springDataReportRepository;
        this.reportQueriesRepository = reportQueriesRepository;
    }
    
    // ===== Relatório 1: Custos =====
    
    @Override
    public BigDecimal getTotalMaintenanceCost(LocalDate startDate, LocalDate endDate) {
        Double cost = springDataReportRepository.getTotalMaintenanceCost(startDate, endDate);
        return cost != null ? BigDecimal.valueOf(cost) : BigDecimal.ZERO;
    }
    
    @Override
    public List<Map<String, Object>> getVehicleCosts(LocalDate startDate, LocalDate endDate) {
        return springDataReportRepository.getVehicleCosts(startDate, endDate);
    }
    
    @Override
    public List<Map<String, Object>> getMonthlyCosts(LocalDate startDate, LocalDate endDate) {
        return springDataReportRepository.getMonthlyCosts(startDate, endDate);
    }
    
    // ===== Relatório 2: Utilização =====
    
    @Override
    public List<Map<String, Object>> getVehicleUtilization(LocalDate startDate, LocalDate endDate) {
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return springDataReportRepository.getVehicleUtilization(startDate, endDate, totalDays);
    }
    
    @Override
    public List<Map<String, Object>> getIdleVehicles(Integer daysIdle) {
        return springDataReportRepository.getIdleVehicles(daysIdle);
    }
    
    // ===== Relatório 3: Manutenção =====
    
    @Override
    public List<Map<String, Object>> getMaintenanceHistory(LocalDate startDate, LocalDate endDate) {
        return reportQueriesRepository.getMaintenanceHistory(startDate, endDate);
    }
    
    @Override
    public List<Map<String, Object>> getUpcomingMaintenances(Integer daysAhead) {
        return reportQueriesRepository.getUpcomingMaintenances(daysAhead);
    }
    
    @Override
    public List<Map<String, Object>> getOverdueMaintenances() {
        return reportQueriesRepository.getOverdueMaintenances();
    }
    
    @Override
    public Map<String, Long> getMaintenanceCountByType(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = reportQueriesRepository.getMaintenanceCountByType(startDate, endDate);
        return results.stream()
            .collect(Collectors.toMap(
                row -> row[0].toString(),
                row -> ((Number) row[1]).longValue()
            ));
    }
    
    // ===== Relatório 4: Motoristas =====
    
    @Override
    public List<Map<String, Object>> getDriverPerformance(LocalDate startDate, LocalDate endDate) {
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return reportQueriesRepository.getDriverPerformance(startDate, endDate, totalDays);
    }
    
    @Override
    public List<Map<String, Object>> getDriversWithCNHExpiringSoon(Integer daysAhead) {
        return reportQueriesRepository.getDriversWithCNHExpiringSoon(daysAhead);
    }
    
    // ===== Relatório 5: Viagens =====
    
    @Override
    public List<Map<String, Object>> getTripsByPeriod(LocalDate startDate, LocalDate endDate) {
        return reportQueriesRepository.getTripsByPeriod(startDate, endDate);
    }
    
    @Override
    public List<Map<String, Object>> getMostFrequentRoutes(LocalDate startDate, LocalDate endDate, Integer limit) {
        return reportQueriesRepository.getMostFrequentRoutes(startDate, endDate, limit);
    }
    
    @Override
    public Map<String, Long> getTripCountByStatus(LocalDate startDate, LocalDate endDate) {
        // Trips table doesn't have status column, returning empty map
        return new java.util.HashMap<>();
    }
    
    // ===== Relatório 6: Depreciação =====
    
    @Override
    public List<Map<String, Object>> getVehicleDepreciation() {
        return reportQueriesRepository.getVehicleDepreciation();
    }
    
    // ===== Relatório 7: Documentação =====
    
    @Override
    public List<Map<String, Object>> getVehiclesWithExpiredDocuments() {
        // Implementar quando houver campos de documentação no Vehicle
        return List.of();
    }
    
    @Override
    public List<Map<String, Object>> getVehiclesWithDocumentsExpiringSoon(Integer daysAhead) {
        // Implementar quando houver campos de documentação no Vehicle
        return List.of();
    }
    
    // ===== Relatório 8: Disponibilidade =====
    
    @Override
    public List<Map<String, Object>> getVehicleAvailability(LocalDate startDate, LocalDate endDate) {
        return getVehicleUtilization(startDate, endDate); // Mesmos dados, perspectiva diferente
    }
}
