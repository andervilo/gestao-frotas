package dev.andervilo.gestao_frotas.application.usecase.vehicle;

import dev.andervilo.gestao_frotas.application.dto.VehicleDTO;
import dev.andervilo.gestao_frotas.application.mapper.VehicleDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.service.VehicleDomainService;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new vehicle.
 */
@Service
@RequiredArgsConstructor
public class CreateVehicleUseCase {
    
    private final VehicleDomainService vehicleDomainService;
    private final VehicleDtoMapper mapper;
    
    @Transactional
    public VehicleDTO execute(VehicleDTO dto) {
        LicensePlate licensePlate = new LicensePlate(dto.getLicensePlate());
        
        Vehicle vehicle = vehicleDomainService.createVehicle(
                licensePlate,
                dto.getType(),
                dto.getBrand(),
                dto.getModel(),
                dto.getYear(),
                dto.getCurrentMileage()
        );
        
        return mapper.toDto(vehicle);
    }
}
