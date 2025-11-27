package dev.andervilo.gestao_frotas.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for generating reports data
 */
public interface ReportRepository {
    
    // ===== Relatório 1: Custos Operacionais =====
    BigDecimal getTotalMaintenanceCost(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getVehicleCosts(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getMonthlyCosts(LocalDate startDate, LocalDate endDate);
    
    // ===== Relatório 2: Utilização de Frota =====
    List<Map<String, Object>> getVehicleUtilization(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getIdleVehicles(Integer daysIdle);
    
    // ===== Relatório 3: Manutenção Preventiva =====
    List<Map<String, Object>> getMaintenanceHistory(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getUpcomingMaintenances(Integer daysAhead);
    List<Map<String, Object>> getOverdueMaintenances();
    Map<String, Long> getMaintenanceCountByType(LocalDate startDate, LocalDate endDate);
    
    // ===== Relatório 4: Performance de Motoristas =====
    List<Map<String, Object>> getDriverPerformance(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getDriversWithCNHExpiringSoon(Integer daysAhead);
    
    // ===== Relatório 5: Viagens Detalhado =====
    List<Map<String, Object>> getTripsByPeriod(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getMostFrequentRoutes(LocalDate startDate, LocalDate endDate, Integer limit);
    Map<String, Long> getTripCountByStatus(LocalDate startDate, LocalDate endDate);
    
    // ===== Relatório 6: Depreciação =====
    List<Map<String, Object>> getVehicleDepreciation();
    
    // ===== Relatório 7: Documentação =====
    List<Map<String, Object>> getVehiclesWithExpiredDocuments();
    List<Map<String, Object>> getVehiclesWithDocumentsExpiringSoon(Integer daysAhead);
    
    // ===== Relatório 8: Disponibilidade =====
    List<Map<String, Object>> getVehicleAvailability(LocalDate startDate, LocalDate endDate);
}
