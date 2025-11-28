package dev.andervilo.gestao_frotas.application.usecase.maintenance;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceDTO;
import dev.andervilo.gestao_frotas.application.dto.MaintenanceFilterDTO;
import dev.andervilo.gestao_frotas.application.mapper.MaintenanceDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import dev.andervilo.gestao_frotas.domain.service.MaintenanceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case for retrieving maintenance records.
 */
@Service
@RequiredArgsConstructor
public class GetMaintenanceUseCase {
    
    private final MaintenanceDomainService maintenanceDomainService;
    private final MaintenanceDtoMapper mapper;
    
    @Transactional(readOnly = true)
    public MaintenanceDTO findById(UUID id) {
        Maintenance maintenance = maintenanceDomainService.findMaintenanceById(id);
        return mapper.toDto(maintenance);
    }
    
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findAll() {
        List<Maintenance> maintenances = maintenanceDomainService.findAllMaintenances();
        return maintenances.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<MaintenanceDTO> findAll(MaintenanceFilterDTO filter, Pageable pageable) {
        Page<Maintenance> maintenances = maintenanceDomainService.findAllMaintenances(filter, pageable);
        return maintenances.map(mapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findByVehicleId(UUID vehicleId) {
        List<Maintenance> maintenances = maintenanceDomainService.findMaintenancesByVehicle(vehicleId);
        return maintenances.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findByStatus(MaintenanceStatus status) {
        List<Maintenance> maintenances = maintenanceDomainService.findMaintenancesByStatus(status);
        return maintenances.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findByType(MaintenanceType type) {
        List<Maintenance> maintenances = maintenanceDomainService.findMaintenancesByType(type);
        return maintenances.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}