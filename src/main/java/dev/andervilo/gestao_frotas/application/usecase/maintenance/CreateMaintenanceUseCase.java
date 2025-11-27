package dev.andervilo.gestao_frotas.application.usecase.maintenance;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceDTO;
import dev.andervilo.gestao_frotas.application.mapper.MaintenanceDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.service.MaintenanceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new maintenance.
 */
@Service
@RequiredArgsConstructor
public class CreateMaintenanceUseCase {
    
    private final MaintenanceDomainService maintenanceDomainService;
    private final MaintenanceDtoMapper mapper;
    
    @Transactional
    public MaintenanceDTO execute(MaintenanceDTO dto) {
        Maintenance maintenance = maintenanceDomainService.createMaintenance(
                dto.getVehicleId(),
                dto.getType(),
                dto.getDescription(),
                dto.getScheduledDate()
        );
        
        return mapper.toDto(maintenance);
    }
}
