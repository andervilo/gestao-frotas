package dev.andervilo.gestao_frotas.domain.enums;

/**
 * Status of a vehicle in the fleet.
 */
public enum VehicleStatus {
    AVAILABLE("Disponível"),
    IN_USE("Em Uso"),
    MAINTENANCE("Em Manutenção"),
    INACTIVE("Inativo");
    
    private final String description;
    
    VehicleStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
