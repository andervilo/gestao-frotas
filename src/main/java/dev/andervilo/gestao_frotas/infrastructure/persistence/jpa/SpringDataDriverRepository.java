package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.DriverJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for DriverJpaEntity.
 */
@Repository
public interface SpringDataDriverRepository extends JpaRepository<DriverJpaEntity, UUID>,
                                                     JpaSpecificationExecutor<DriverJpaEntity> {
    
    Optional<DriverJpaEntity> findByCpf(String cpf);
    
    Optional<DriverJpaEntity> findByCnh(String cnh);
    
    List<DriverJpaEntity> findByStatus(DriverStatus status);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByCnh(String cnh);
}
