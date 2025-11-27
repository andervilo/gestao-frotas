package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Vehicle aggregate.
 * This is a domain interface that will be implemented in the infrastructure layer.
 */
public interface VehicleRepository {
    
    Vehicle save(Vehicle vehicle);
    
    Optional<Vehicle> findById(UUID id);
    
    Optional<Vehicle> findByLicensePlate(LicensePlate licensePlate);
    
    List<Vehicle> findAll();
    
    List<Vehicle> findByStatus(VehicleStatus status);
    
    List<Vehicle> findByType(VehicleType type);
    
    void deleteById(UUID id);
    
    boolean existsByLicensePlate(LicensePlate licensePlate);
}
