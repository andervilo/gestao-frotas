package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.MaintenanceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for MaintenanceJpaEntity.
 */
@Repository
public interface SpringDataMaintenanceRepository extends JpaRepository<MaintenanceJpaEntity, UUID>,
                                                          JpaSpecificationExecutor<MaintenanceJpaEntity> {
    
    @Query("SELECT m FROM MaintenanceJpaEntity m WHERE m.vehicle.id = :vehicleId")
    List<MaintenanceJpaEntity> findByVehicleId(@Param("vehicleId") UUID vehicleId);
    
    List<MaintenanceJpaEntity> findByStatus(MaintenanceStatus status);
    
    List<MaintenanceJpaEntity> findByType(MaintenanceType type);
}
