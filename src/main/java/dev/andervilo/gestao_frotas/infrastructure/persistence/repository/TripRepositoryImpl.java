package dev.andervilo.gestao_frotas.infrastructure.persistence.repository;

import dev.andervilo.gestao_frotas.application.dto.TripFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.repository.TripRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataTripRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.mapper.TripJpaMapper;
import dev.andervilo.gestao_frotas.infrastructure.persistence.specification.TripSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of TripRepository using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepository {
    
    private final SpringDataTripRepository jpaRepository;
    private final TripJpaMapper mapper;
    
    @Override
    public Trip save(Trip trip) {
        var jpaEntity = mapper.toJpaEntity(trip);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Trip> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Trip> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Trip> findAll(TripFilterDTO filter, Pageable pageable) {
        var spec = TripSpecification.withFilters(filter);
        return jpaRepository.findAll(spec, pageable)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Trip> findByVehicleId(UUID vehicleId) {
        return jpaRepository.findByVehicleId(vehicleId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Trip> findByDriverId(UUID driverId) {
        return jpaRepository.findByDriverId(driverId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Trip> findInProgressTrips() {
        return jpaRepository.findInProgressTrips().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Trip> findCompletedTrips() {
        return jpaRepository.findCompletedTrips().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Trip> findScheduledTrips() {
        return jpaRepository.findScheduledTrips().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
