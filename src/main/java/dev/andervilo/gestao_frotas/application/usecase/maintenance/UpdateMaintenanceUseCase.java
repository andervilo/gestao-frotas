package dev.andervilo.gestao_frotas.application.usecase.maintenance;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceDTO;
import dev.andervilo.gestao_frotas.application.mapper.MaintenanceDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.service.MaintenanceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use case for updating maintenance records.
 */
@Service
@RequiredArgsConstructor
public class UpdateMaintenanceUseCase {
    
    private final MaintenanceDomainService maintenanceDomainService;
    private final MaintenanceDtoMapper mapper;
    
    @Transactional
    public MaintenanceDTO startMaintenance(UUID id) {
        Maintenance maintenance = maintenanceDomainService.startMaintenance(id);
        return mapper.toDto(maintenance);
    }
    
    @Transactional
    public MaintenanceDTO completeMaintenance(UUID id, BigDecimal finalCost, String notes) {
        Maintenance maintenance = maintenanceDomainService.completeMaintenance(id, finalCost, notes);
        return mapper.toDto(maintenance);
    }
    
    @Transactional
    public MaintenanceDTO cancelMaintenance(UUID id, String reason) {
        Maintenance maintenance = maintenanceDomainService.cancelMaintenance(id, reason);
        return mapper.toDto(maintenance);
    }
}