package dev.andervilo.gestao_frotas.application.usecase.vehicle;

import dev.andervilo.gestao_frotas.application.dto.VehicleDTO;
import dev.andervilo.gestao_frotas.application.mapper.VehicleDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.service.VehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for updating a vehicle.
 */
@Service
@RequiredArgsConstructor
public class UpdateVehicleUseCase {
    
    private final VehicleDomainService vehicleDomainService;
    private final VehicleDtoMapper mapper;
    
    @Transactional
    public VehicleDTO execute(UUID id, VehicleDTO dto) {
        Vehicle updated = vehicleDomainService.updateVehicle(
                id,
                dto.getBrand(),
                dto.getModel(),
                dto.getYear(),
                dto.getCurrentMileage(),
                dto.getStatus()
        );
        
        return mapper.toDto(updated);
    }
}
