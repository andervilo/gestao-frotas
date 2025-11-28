package dev.andervilo.gestao_frotas.infrastructure.persistence.repository;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import dev.andervilo.gestao_frotas.domain.repository.MaintenanceRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataMaintenanceRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.mapper.MaintenanceJpaMapper;
import dev.andervilo.gestao_frotas.infrastructure.persistence.specification.MaintenanceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of MaintenanceRepository using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class MaintenanceRepositoryImpl implements MaintenanceRepository {
    
    private final SpringDataMaintenanceRepository jpaRepository;
    private final MaintenanceJpaMapper mapper;
    
    @Override
    public Maintenance save(Maintenance maintenance) {
        var jpaEntity = mapper.toJpaEntity(maintenance);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Maintenance> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Maintenance> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Maintenance> findAll(MaintenanceFilterDTO filter, Pageable pageable) {
        var spec = MaintenanceSpecification.withFilters(filter);
        return jpaRepository.findAll(spec, pageable)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Maintenance> findByVehicleId(UUID vehicleId) {
        return jpaRepository.findByVehicleId(vehicleId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Maintenance> findByStatus(MaintenanceStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Maintenance> findByType(MaintenanceType type) {
        return jpaRepository.findByType(type).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
