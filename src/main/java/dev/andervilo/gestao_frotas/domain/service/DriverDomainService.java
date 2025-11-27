package dev.andervilo.gestao_frotas.domain.service;

import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import dev.andervilo.gestao_frotas.domain.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Domain service for Driver aggregate.
 */
@Service
@RequiredArgsConstructor
public class DriverDomainService {
    
    private final DriverRepository driverRepository;
    
    /**
     * Creates a new driver ensuring business rules.
     */
    public Driver createDriver(
            String name,
            String cpf,
            String cnh,
            String cnhCategory,
            LocalDate cnhExpirationDate) {
        
        // Business rule: CPF must be unique
        if (driverRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("Driver with CPF " + cpf + " already exists");
        }
        
        // Business rule: CNH must be unique
        if (driverRepository.existsByCnh(cnh)) {
            throw new IllegalArgumentException("Driver with CNH " + cnh + " already exists");
        }
        
        // Create domain entity with business logic
        Driver driver = Driver.create(name, cpf, cnh, cnhCategory, cnhExpirationDate);
        
        return driverRepository.save(driver);
    }
    
    /**
     * Updates driver information.
     */
    public Driver updateDriver(UUID id, String name, String cnhCategory, LocalDate cnhExpirationDate) {
        Driver driver = findDriverById(id);
        driver.update(name, cnhCategory, cnhExpirationDate);
        return driverRepository.save(driver);
    }
    
    /**
     * Finds driver by ID.
     */
    public Driver findDriverById(UUID id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));
    }
    
    /**
     * Finds all drivers.
     */
    public List<Driver> findAllDrivers() {
        return driverRepository.findAll();
    }
    
    /**
     * Finds drivers by status.
     */
    public List<Driver> findDriversByStatus(DriverStatus status) {
        return driverRepository.findByStatus(status);
    }
    
    /**
     * Deletes a driver.
     */
    public void deleteDriver(UUID id) {
        if (!driverRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }
    
    /**
     * Suspends a driver (business operation).
     */
    public Driver suspendDriver(UUID id) {
        Driver driver = findDriverById(id);
        driver.suspend();
        return driverRepository.save(driver);
    }
    
    /**
     * Activates a driver (business operation).
     */
    public Driver activateDriver(UUID id) {
        Driver driver = findDriverById(id);
        driver.activate();
        return driverRepository.save(driver);
    }
    
    /**
     * Deactivates a driver (business operation).
     */
    public Driver deactivateDriver(UUID id) {
        Driver driver = findDriverById(id);
        driver.deactivate();
        return driverRepository.save(driver);
    }
}
