package dev.andervilo.gestao_frotas.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for Trip persistence.
 */
@Entity
@Table(name = "trips")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripJpaEntity {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleJpaEntity vehicle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverJpaEntity driver;
    
    @Column(name = "origin", nullable = false, length = 200)
    private String origin;
    
    @Column(name = "destination", nullable = false, length = 200)
    private String destination;
    
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;
    
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
    
    @Column(name = "start_mileage", nullable = false)
    private Long startMileage;
    
    @Column(name = "end_mileage")
    private Long endMileage;
    
    @Column(name = "distance_traveled")
    private Long distanceTraveled;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
