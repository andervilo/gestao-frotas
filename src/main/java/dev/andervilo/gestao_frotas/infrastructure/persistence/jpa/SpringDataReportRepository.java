package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.MaintenanceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface SpringDataReportRepository extends JpaRepository<MaintenanceJpaEntity, UUID> {
    
    // ===== Relatório 1: Custos Operacionais =====
    
    @Query("""
        SELECT COALESCE(SUM(m.cost), 0) 
        FROM MaintenanceJpaEntity m 
        WHERE CAST(m.scheduledDate AS DATE) BETWEEN :startDate AND :endDate
        AND m.status = 'COMPLETED'
    """)
    Double getTotalMaintenanceCost(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    @Query(value = """
        SELECT 
            v.id::text as vehicleId,
            v.license_plate as licensePlate,
            v.brand,
            v.model,
            COALESCE(SUM(m.cost), 0) as totalCost,
            COALESCE(SUM(m.cost), 0) as maintenanceCost,
            COALESCE(SUM(t.distance_traveled), 0) as totalKm,
            CASE 
                WHEN SUM(t.distance_traveled) > 0 
                THEN COALESCE(SUM(m.cost), 0) / SUM(t.distance_traveled)
                ELSE 0 
            END as costPerKm
        FROM vehicles v
        LEFT JOIN maintenances m ON m.vehicle_id = v.id 
            AND m.scheduled_date BETWEEN :startDate AND :endDate
            AND m.status = 'COMPLETED'
        LEFT JOIN trips t ON t.vehicle_id = v.id 
            AND DATE(t.start_date_time) BETWEEN :startDate AND :endDate
        GROUP BY v.id, v.license_plate, v.brand, v.model
        ORDER BY totalCost DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getVehicleCosts(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query(value = """
        SELECT 
            TO_CHAR(m.scheduled_date, 'YYYY-MM') as month,
            COALESCE(SUM(m.cost), 0) as totalCost,
            COALESCE(SUM(m.cost), 0) as maintenanceCost,
            0 as totalKm
        FROM maintenances m
        WHERE m.scheduled_date BETWEEN :startDate AND :endDate
            AND m.status = 'COMPLETED'
        GROUP BY TO_CHAR(m.scheduled_date, 'YYYY-MM')
        ORDER BY month
    """, nativeQuery = true)
    List<Map<String, Object>> getMonthlyCosts(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    // ===== Relatório 2: Utilização de Frota =====
    
    @Query(value = """
        SELECT 
            v.id::text as vehicleId,
            v.license_plate as licensePlate,
            v.brand,
            v.model,
            COUNT(DISTINCT t.id) as totalTrips,
            COALESCE(SUM(t.distance_traveled), 0) as totalKm,
            COUNT(DISTINCT DATE(t.start_date_time)) as daysInUse,
            COUNT(DISTINCT CASE 
                WHEN m.status IN ('SCHEDULED', 'IN_PROGRESS') 
                THEN DATE(m.scheduled_date) 
            END) as daysInMaintenance,
            :totalDays - COUNT(DISTINCT DATE(t.start_date_time)) as daysIdle,
            CASE 
                WHEN :totalDays > 0 
                THEN (COUNT(DISTINCT DATE(t.start_date_time))::decimal / :totalDays) * 100
                ELSE 0 
            END as utilizationRate
        FROM vehicles v
        LEFT JOIN trips t ON t.vehicle_id = v.id 
            AND DATE(t.start_date_time) BETWEEN :startDate AND :endDate
        LEFT JOIN maintenances m ON m.vehicle_id = v.id
            AND m.scheduled_date BETWEEN :startDate AND :endDate
        GROUP BY v.id, v.license_plate, v.brand, v.model
        ORDER BY utilizationRate DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getVehicleUtilization(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("totalDays") int totalDays
    );
    
    @Query(value = """
        SELECT 
            v.id::text as vehicleId,
            v.license_plate as licensePlate,
            v.brand,
            v.model,
            COALESCE(CURRENT_DATE - MAX(DATE(t.end_date_time)), 9999) as daysIdle,
            MAX(DATE(t.end_date_time)) as lastTripDate
        FROM vehicles v
        LEFT JOIN trips t ON t.vehicle_id = v.id 
            AND t.end_date_time IS NOT NULL
        GROUP BY v.id, v.license_plate, v.brand, v.model
        HAVING COALESCE(CURRENT_DATE - MAX(DATE(t.end_date_time)), 9999) >= :daysIdle
        ORDER BY daysIdle DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getIdleVehicles(@Param("daysIdle") int daysIdle);
}
