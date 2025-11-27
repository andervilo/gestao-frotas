package dev.andervilo.gestao_frotas.domain.enums;

/**
 * Status of a maintenance record.
 */
public enum MaintenanceStatus {
    SCHEDULED("Agendada"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada");
    
    private final String description;
    
    MaintenanceStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
