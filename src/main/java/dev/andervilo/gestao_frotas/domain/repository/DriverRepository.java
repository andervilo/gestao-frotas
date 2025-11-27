package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.application.dto.DriverFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    
    Page<Driver> findAll(DriverFilterDTO filter, Pageable pageable);
    
    List<Driver> findByStatus(DriverStatus status);
    
    void deleteById(UUID id);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByCnh(String cnh);
}
