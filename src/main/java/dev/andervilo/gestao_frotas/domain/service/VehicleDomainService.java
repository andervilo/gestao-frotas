package dev.andervilo.gestao_frotas.domain.service;

import dev.andervilo.gestao_frotas.application.dto.VehicleFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import dev.andervilo.gestao_frotas.domain.repository.VehicleRepository;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Domain service for Vehicle aggregate.
 * Encapsulates business logic and orchestrates domain operations.
 */
@Service
@RequiredArgsConstructor
public class VehicleDomainService {
    
    private final VehicleRepository vehicleRepository;
    
    /**
     * Creates a new vehicle ensuring business rules.
     */
    public Vehicle createVehicle(
            LicensePlate licensePlate,
            VehicleType type,
            String brand,
            String model,
            Integer year,
            Long initialMileage) {
        
        // Business rule: License plate must be unique
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException(
                "Vehicle with license plate " + licensePlate.getValue() + " already exists"
            );
        }
        
        // Create domain entity with business logic
        Vehicle vehicle = Vehicle.create(licensePlate, type, brand, model, year, initialMileage);
        
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Updates vehicle information.
     */
    public Vehicle updateVehicle(UUID id, String brand, String model, Integer year, Long mileage, VehicleStatus status) {
        Vehicle vehicle = findVehicleById(id);
        
        // Apply business logic through domain entity
        vehicle.update(brand, model, year);
        
        if (mileage != null && !mileage.equals(vehicle.getCurrentMileage())) {
            vehicle.updateMileage(mileage);
        }
        
        if (status != null && status != vehicle.getStatus()) {
            vehicle.changeStatus(status);
        }
        
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Finds vehicle by ID.
     */
    public Vehicle findVehicleById(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
    }
    
    /**
     * Finds all vehicles.
     */
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    /**
     * Finds all vehicles with filters and pagination.
     */
    public Page<Vehicle> findAllVehicles(VehicleFilterDTO filter, Pageable pageable) {
        return vehicleRepository.findAll(filter, pageable);
    }
    
    /**
     * Finds vehicles by status.
     */
    public List<Vehicle> findVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }
    
    /**
     * Finds vehicles by type.
     */
    public List<Vehicle> findVehiclesByType(VehicleType type) {
        return vehicleRepository.findByType(type);
    }
    
    /**
     * Deletes a vehicle.
     */
    public void deleteVehicle(UUID id) {
        if (!vehicleRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
    
    /**
     * Marks vehicle as in use (business operation).
     */
    public Vehicle markVehicleAsInUse(UUID id) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.markAsInUse();
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Marks vehicle as available (business operation).
     */
    public Vehicle markVehicleAsAvailable(UUID id) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.markAsAvailable();
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Marks vehicle as in maintenance (business operation).
     */
    public Vehicle markVehicleAsInMaintenance(UUID id) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.markAsInMaintenance();
        return vehicleRepository.save(vehicle);
    }
}
