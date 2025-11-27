package dev.andervilo.gestao_frotas.application.usecase.maintenance;

import dev.andervilo.gestao_frotas.domain.service.MaintenanceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for deleting a maintenance record.
 */
@Service
@RequiredArgsConstructor
public class DeleteMaintenanceUseCase {
    
    private final MaintenanceDomainService maintenanceDomainService;
    
    @Transactional
    public void execute(UUID id) {
        maintenanceDomainService.deleteMaintenance(id);
    }
}