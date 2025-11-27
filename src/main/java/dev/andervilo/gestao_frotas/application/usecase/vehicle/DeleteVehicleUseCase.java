package dev.andervilo.gestao_frotas.application.usecase.vehicle;

import dev.andervilo.gestao_frotas.domain.service.VehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for deleting a vehicle.
 */
@Service
@RequiredArgsConstructor
public class DeleteVehicleUseCase {
    
    private final VehicleDomainService vehicleDomainService;
    
    @Transactional
    public void execute(UUID id) {
        vehicleDomainService.deleteVehicle(id);
    }
}
