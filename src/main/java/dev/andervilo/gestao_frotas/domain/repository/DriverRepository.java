package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Driver aggregate.
 */
public interface DriverRepository {
    
    Driver save(Driver driver);
    
    Optional<Driver> findById(UUID id);
    
    Optional<Driver> findByCpf(String cpf);
    
    Optional<Driver> findByCnh(String cnh);
    
    List<Driver> findAll();
    
    List<Driver> findByStatus(DriverStatus status);
    
    void deleteById(UUID id);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByCnh(String cnh);
}
