package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Additional report queries (Relatórios 3-8)
 * Implemented as a repository service using EntityManager
 */
@Repository
public class ReportQueriesRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // ===== Relatório 3: Manutenção =====
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getMaintenanceHistory(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                CAST(v.id AS VARCHAR) as vehicleid,
                v.license_plate as licenseplate,
                m.type,
                m.description,
                DATE(m.scheduled_date) as scheduleddate,
                DATE(m.completion_date) as completiondate,
                m.cost,
                m.status
            FROM maintenances m
            JOIN vehicles v ON v.id = m.vehicle_id
            WHERE DATE(m.scheduled_date) BETWEEN :startDate AND :endDate
            ORDER BY m.scheduled_date DESC
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getUpcomingMaintenances(int daysAhead) {
        String sql = """
            SELECT 
                CAST(v.id AS VARCHAR) as vehicleid,
                v.license_plate as licenseplate,
                v.brand,
                v.model,
                m.type,
                DATE(m.scheduled_date) as scheduleddate,
                CAST(EXTRACT(DAY FROM (m.scheduled_date - CURRENT_TIMESTAMP)) AS INTEGER) as daysuntil
            FROM maintenances m
            JOIN vehicles v ON v.id = m.vehicle_id
            WHERE m.status = 'SCHEDULED'
                AND m.scheduled_date BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + (:daysAhead || ' days')::INTERVAL
            ORDER BY m.scheduled_date
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("daysAhead", daysAhead);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getOverdueMaintenances() {
        String sql = """
            SELECT 
                CAST(v.id AS VARCHAR) as vehicleid,
                v.license_plate as licenseplate,
                v.brand,
                v.model,
                m.type,
                DATE(m.scheduled_date) as scheduleddate,
                CAST(EXTRACT(DAY FROM (CURRENT_TIMESTAMP - m.scheduled_date)) AS INTEGER) as daysoverdue
            FROM maintenances m
            JOIN vehicles v ON v.id = m.vehicle_id
            WHERE m.status = 'SCHEDULED'
                AND m.scheduled_date < CURRENT_TIMESTAMP
            ORDER BY daysOverdue DESC
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> getMaintenanceCountByType(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT m.type, COUNT(*) as count
            FROM maintenances m
            WHERE DATE(m.scheduled_date) BETWEEN :startDate AND :endDate
            GROUP BY m.type
        """;
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        
        return query.getResultList();
    }
    
    // ===== Relatório 4: Motoristas =====
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getDriverPerformance(LocalDate startDate, LocalDate endDate, int totalDays) {
        String sql = """
            SELECT 
                d.id::text as driverId,
                d.name,
                d.cnh,
                COUNT(t.id) as totalTrips,
                COALESCE(SUM(t.distance_traveled), 0) as totalKm,
                CASE 
                    WHEN COUNT(t.id) > 0 
                    THEN (COUNT(t.id)::decimal / :totalDays) * 100
                    ELSE 0 
                END as utilizationRate
            FROM drivers d
            LEFT JOIN trips t ON t.driver_id = d.id
                AND DATE(t.start_date_time) BETWEEN :startDate AND :endDate
            GROUP BY d.id, d.name, d.cnh
            ORDER BY totalTrips DESC
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("totalDays", totalDays);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getDriversWithCNHExpiringSoon(int daysAhead) {
        String sql = """
            SELECT 
                d.id::text as driverId,
                d.name,
                d.cnh,
                d.cnh_expiration_date as cnhExpiration,
                d.cnh_expiration_date - CURRENT_DATE as daysUntilExpiration
            FROM drivers d
            WHERE d.cnh_expiration_date IS NOT NULL
                AND d.cnh_expiration_date BETWEEN CURRENT_DATE AND CURRENT_DATE + :daysAhead
            ORDER BY d.cnh_expiration_date
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("daysAhead", daysAhead);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    // ===== Relatório 5: Viagens =====
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getTripsByPeriod(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                t.id::text as tripId,
                v.license_plate as vehiclePlate,
                d.name as driverName,
                t.origin,
                t.destination,
                t.start_date_time as startDate,
                t.end_date_time as endDate,
                t.distance_traveled as distance,
                t.notes
            FROM trips t
            JOIN vehicles v ON v.id = t.vehicle_id
            JOIN drivers d ON d.id = t.driver_id
            WHERE DATE(t.start_date_time) BETWEEN :startDate AND :endDate
            ORDER BY t.start_date_time DESC
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getMostFrequentRoutes(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
            SELECT 
                t.origin,
                t.destination,
                COUNT(t.id) as tripCount,
                SUM(t.distance_traveled) as totalKm,
                AVG(t.distance_traveled) as averageKm
            FROM trips t
            WHERE DATE(t.start_date_time) BETWEEN :startDate AND :endDate
            GROUP BY t.origin, t.destination
            ORDER BY tripCount DESC
            LIMIT :limit
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("limit", limit);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
    
    // Trips table doesn't have status column - method disabled
    /*
    public List<Object[]> getTripCountByStatus(LocalDate startDate, LocalDate endDate) {
        String jpql = """
            SELECT t.status as status, COUNT(t) as count
            FROM TripJpaEntity t
            WHERE t.startDateTime BETWEEN :startDateTime AND :endDateTime
            GROUP BY t.status
        """;
        
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDateTime", startDate.atStartOfDay())
                .setParameter("endDateTime", endDate.atTime(23, 59, 59))
                .getResultList();
    }
    */
    
    // ===== Relatório 6: Depreciação =====
    
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<Map<String, Object>> getVehicleDepreciation() {
        String sql = """
            SELECT 
                v.id::text as vehicleId,
                v.license_plate as licensePlate,
                v.brand,
                v.model,
                v.year,
                0 as purchaseValue,
                0 as currentValue,
                0 as totalDepreciation,
                0 as depreciationRate,
                EXTRACT(YEAR FROM CURRENT_DATE) - v.year as ageInYears,
                CURRENT_DATE as purchaseDate
            FROM vehicles v
            ORDER BY ageInYears DESC
        """;
        
        org.hibernate.query.NativeQuery<Map<String, Object>> query = 
            (org.hibernate.query.NativeQuery<Map<String, Object>>) entityManager.createNativeQuery(sql);
        query.setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);
        
        return query.getResultList();
    }
}
