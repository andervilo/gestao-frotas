package dev.andervilo.gestao_frotas.domain.enums;

/**
 * Status of a driver.
 */
public enum DriverStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso");
    
    private final String description;
    
    DriverStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
