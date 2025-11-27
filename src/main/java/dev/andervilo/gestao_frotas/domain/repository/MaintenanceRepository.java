package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Maintenance aggregate.
 */
public interface MaintenanceRepository {
    
    Maintenance save(Maintenance maintenance);
    
    Optional<Maintenance> findById(UUID id);
    
    List<Maintenance> findAll();
    
    List<Maintenance> findByVehicleId(UUID vehicleId);
    
    List<Maintenance> findByStatus(MaintenanceStatus status);
    
    List<Maintenance> findByType(MaintenanceType type);
    
    void deleteById(UUID id);
}
