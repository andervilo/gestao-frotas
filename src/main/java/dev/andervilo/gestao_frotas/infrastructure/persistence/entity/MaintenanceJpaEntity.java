package dev.andervilo.gestao_frotas.infrastructure.persistence.entity;

import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for Maintenance persistence.
 */
@Entity
@Table(name = "maintenances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceJpaEntity {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleJpaEntity vehicle;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private MaintenanceType type;
    
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;
    
    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MaintenanceStatus status;
    
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
