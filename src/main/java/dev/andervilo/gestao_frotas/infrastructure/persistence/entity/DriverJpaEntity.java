package dev.andervilo.gestao_frotas.infrastructure.persistence.entity;

import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for Driver persistence.
 */
@Entity
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverJpaEntity {
    
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "cpf", unique = true, nullable = false, length = 14)
    private String cpf;
    
    @Column(name = "cnh", unique = true, nullable = false, length = 20)
    private String cnh;
    
    @Column(name = "cnh_category", nullable = false, length = 5)
    private String cnhCategory;
    
    @Column(name = "cnh_expiration_date", nullable = false)
    private LocalDate cnhExpirationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DriverStatus status;
    
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
