package dev.andervilo.gestao_frotas.domain.enums;

/**
 * Type of maintenance performed on a vehicle.
 */
public enum MaintenanceType {
    PREVENTIVE("Preventiva"),
    CORRECTIVE("Corretiva");
    
    private final String description;
    
    MaintenanceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
