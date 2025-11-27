package dev.andervilo.gestao_frotas.application.usecase.vehicle;

import dev.andervilo.gestao_frotas.application.dto.VehicleDTO;
import dev.andervilo.gestao_frotas.application.mapper.VehicleDtoMapper;
import dev.andervilo.gestao_frotas.domain.service.VehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case for retrieving vehicles.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetVehicleUseCase {
    
    private final VehicleDomainService vehicleDomainService;
    private final VehicleDtoMapper mapper;
    
    public VehicleDTO findById(UUID id) {
        return mapper.toDto(vehicleDomainService.findVehicleById(id));
    }
    
    public List<VehicleDTO> findAll() {
        return vehicleDomainService.findAllVehicles().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
