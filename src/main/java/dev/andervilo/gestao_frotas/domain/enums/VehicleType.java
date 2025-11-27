package dev.andervilo.gestao_frotas.domain.enums;

/**
 * Types of vehicles in the fleet.
 */
public enum VehicleType {
    CAR("Carro"),
    TRUCK("Caminhão"),
    MOTORCYCLE("Motocicleta"),
    VAN("Van"),
    BUS("Ônibus");
    
    private final String description;
    
    VehicleType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
