package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.application.dto.TripFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Trip aggregate.
 */
public interface TripRepository {
    
    Trip save(Trip trip);
    
    Optional<Trip> findById(UUID id);
    
    List<Trip> findAll();
    
    Page<Trip> findAll(TripFilterDTO filter, Pageable pageable);
    
    List<Trip> findByVehicleId(UUID vehicleId);
    
    List<Trip> findByDriverId(UUID driverId);
    
    List<Trip> findInProgressTrips();
    
    List<Trip> findCompletedTrips();
    
    List<Trip> findScheduledTrips();
    
    void deleteById(UUID id);
}
