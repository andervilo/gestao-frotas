package dev.andervilo.gestao_frotas.domain.repository;

import dev.andervilo.gestao_frotas.domain.entity.Trip;

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
    
    List<Trip> findByVehicleId(UUID vehicleId);
    
    List<Trip> findByDriverId(UUID driverId);
    
    List<Trip> findInProgressTrips();
    
    List<Trip> findCompletedTrips();
    
    List<Trip> findScheduledTrips();
    
    void deleteById(UUID id);
}
