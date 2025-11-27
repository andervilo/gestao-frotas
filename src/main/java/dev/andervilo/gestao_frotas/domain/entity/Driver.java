package dev.andervilo.gestao_frotas.domain.entity;

import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a driver.
 */
@Getter
@Builder
@AllArgsConstructor
public class Driver {
    
    private UUID id;
    private String name;
    private String cpf;
    private String cnh; // Driver's license number
    private String cnhCategory; // A, B, C, D, E, AB, AC, AD, AE
    private LocalDate cnhExpirationDate;
    private DriverStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Creates a new driver with validation.
     */
    public static Driver create(
            String name,
            String cpf,
            String cnh,
            String cnhCategory,
            LocalDate cnhExpirationDate) {
        
        validate(name, cpf, cnh, cnhCategory, cnhExpirationDate);
        
        return Driver.builder()
                .id(UUID.randomUUID())
                .name(name)
                .cpf(cpf)
                .cnh(cnh)
                .cnhCategory(cnhCategory.toUpperCase())
                .cnhExpirationDate(cnhExpirationDate)
                .status(DriverStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Updates driver information.
     */
    public void update(String name, String cnhCategory, LocalDate cnhExpirationDate) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (cnhCategory != null && !cnhCategory.isBlank()) {
            validateCnhCategory(cnhCategory);
            this.cnhCategory = cnhCategory.toUpperCase();
        }
        if (cnhExpirationDate != null) {
            this.cnhExpirationDate = cnhExpirationDate;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Changes driver status.
     */
    public void changeStatus(DriverStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Suspends the driver.
     */
    public void suspend() {
        changeStatus(DriverStatus.SUSPENDED);
    }
    
    /**
     * Activates the driver.
     */
    public void activate() {
        changeStatus(DriverStatus.ACTIVE);
    }
    
    /**
     * Deactivates the driver.
     */
    public void deactivate() {
        changeStatus(DriverStatus.INACTIVE);
    }
    
    /**
     * Checks if driver's license is expired.
     */
    public boolean isCnhExpired() {
        return cnhExpirationDate.isBefore(LocalDate.now());
    }
    
    /**
     * Checks if driver is active and has valid license.
     */
    public boolean isAvailableForWork() {
        return status == DriverStatus.ACTIVE && !isCnhExpired();
    }
    
    private static void validate(
            String name,
            String cpf,
            String cnh,
            String cnhCategory,
            LocalDate cnhExpirationDate) {
        
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (cpf == null || !isValidCpf(cpf)) {
            throw new IllegalArgumentException("Invalid CPF");
        }
        if (cnh == null || cnh.isBlank()) {
            throw new IllegalArgumentException("CNH cannot be null or empty");
        }
        validateCnhCategory(cnhCategory);
        if (cnhExpirationDate == null) {
            throw new IllegalArgumentException("CNH expiration date cannot be null");
        }
    }
    
    private static void validateCnhCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("CNH category cannot be null or empty");
        }
        String upperCategory = category.toUpperCase();
        if (!upperCategory.matches("^(A|B|C|D|E|AB|AC|AD|AE)$")) {
            throw new IllegalArgumentException("Invalid CNH category: " + category);
        }
    }
    
    private static boolean isValidCpf(String cpf) {
        // Basic CPF validation (remove formatting and check length)
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        return cleanCpf.length() == 11;
    }
}
