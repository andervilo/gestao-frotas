package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for VehicleJpaEntity.
 */
@Repository
public interface SpringDataVehicleRepository extends JpaRepository<VehicleJpaEntity, UUID>, 
                                                      JpaSpecificationExecutor<VehicleJpaEntity> {
    
    Optional<VehicleJpaEntity> findByLicensePlate(String licensePlate);
    
    List<VehicleJpaEntity> findByStatus(VehicleStatus status);
    
    List<VehicleJpaEntity> findByType(VehicleType type);
    
    boolean existsByLicensePlate(String licensePlate);
}
