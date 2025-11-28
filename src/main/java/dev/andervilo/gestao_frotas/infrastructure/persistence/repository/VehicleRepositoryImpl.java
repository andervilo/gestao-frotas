package dev.andervilo.gestao_frotas.infrastructure.persistence.repository;

import dev.andervilo.gestao_frotas.application.dto.VehicleFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.enums.VehicleStatus;
import dev.andervilo.gestao_frotas.domain.enums.VehicleType;
import dev.andervilo.gestao_frotas.domain.repository.VehicleRepository;
import dev.andervilo.gestao_frotas.domain.specification.VehicleSpecification;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataVehicleRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.mapper.VehicleJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of VehicleRepository using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {
    
    private final SpringDataVehicleRepository jpaRepository;
    private final VehicleJpaMapper mapper;
    
    @Override
    public Vehicle save(Vehicle vehicle) {
        var jpaEntity = mapper.toJpaEntity(vehicle);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Vehicle> findByLicensePlate(LicensePlate licensePlate) {
        return jpaRepository.findByLicensePlate(licensePlate.getValue())
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Vehicle> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Vehicle> findAll(VehicleFilterDTO filter, Pageable pageable) {
        var spec = VehicleSpecification.withFilters(filter);
        return jpaRepository.findAll(spec, pageable)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Vehicle> findByStatus(VehicleStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vehicle> findByType(VehicleType type) {
        return jpaRepository.findByType(type).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByLicensePlate(LicensePlate licensePlate) {
        return jpaRepository.existsByLicensePlate(licensePlate.getValue());
    }
}
