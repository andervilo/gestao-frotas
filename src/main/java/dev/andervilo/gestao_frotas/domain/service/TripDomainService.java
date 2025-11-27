package dev.andervilo.gestao_frotas.domain.service;

import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Domain service for Trip aggregate.
 */
@Service
@RequiredArgsConstructor
public class TripDomainService {
    
    private final TripRepository tripRepository;
    private final VehicleDomainService vehicleDomainService;
    private final DriverDomainService driverDomainService;
    
    /**
     * Creates a new trip.
     */
    public Trip createTrip(
            UUID vehicleId,
            UUID driverId,
            String origin,
            String destination,
            Long startMileage) {
        
        // Load vehicle and driver to ensure they exist
        Vehicle vehicle = vehicleDomainService.findVehicleById(vehicleId);
        Driver driver = driverDomainService.findDriverById(driverId);
        
        // Business rule: Vehicle must be available
        if (!vehicle.isAvailable()) {
            throw new IllegalStateException("Vehicle is not available for trip");
        }
        
        // Business rule: Driver must be available for work
        if (!driver.isAvailableForWork()) {
            throw new IllegalStateException("Driver is not available for work");
        }
        
        // Business rule: Start mileage must match vehicle's current mileage
        if (!startMileage.equals(vehicle.getCurrentMileage())) {
            throw new IllegalArgumentException(
                "Start mileage must match vehicle's current mileage: " + vehicle.getCurrentMileage()
            );
        }
        
        // Mark vehicle as in use
        vehicleDomainService.markVehicleAsInUse(vehicleId);
        
        // Create domain entity
        Trip trip = Trip.create(vehicle, driver, origin, destination, startMileage);
        
        return tripRepository.save(trip);
    }
    
    /**
     * Completes a trip.
     */
    public Trip completeTrip(UUID id, Long endMileage, String notes) {
        Trip trip = findTripById(id);
        
        // Complete trip (validates end mileage > start mileage)
        trip.complete(endMileage, notes);
        
        // Update vehicle mileage
        vehicleDomainService.updateVehicle(
            trip.getVehicle().getId(),
            null, null, null,
            endMileage,
            null
        );
        
        // Mark vehicle as available
        vehicleDomainService.markVehicleAsAvailable(trip.getVehicle().getId());
        
        return tripRepository.save(trip);
    }
    
    /**
     * Finds trip by ID.
     */
    public Trip findTripById(UUID id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found with id: " + id));
    }
    
    /**
     * Finds all trips.
     */
    public List<Trip> findAllTrips() {
        return tripRepository.findAll();
    }
    
    /**
     * Finds trips by vehicle.
     */
    public List<Trip> findTripsByVehicle(UUID vehicleId) {
        return tripRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Finds trips by driver.
     */
    public List<Trip> findTripsByDriver(UUID driverId) {
        return tripRepository.findByDriverId(driverId);
    }
    
    /**
     * Finds in-progress trips.
     */
    public List<Trip> findInProgressTrips() {
        return tripRepository.findInProgressTrips();
    }
    
    /**
     * Finds completed trips.
     */
    public List<Trip> findCompletedTrips() {
        return tripRepository.findCompletedTrips();
    }
    
    /**
     * Finds scheduled trips (not started yet).
     */
    public List<Trip> findScheduledTrips() {
        return tripRepository.findScheduledTrips();
    }
    
    /**
     * Deletes a trip.
     */
    public void deleteTrip(UUID id) {
        if (!tripRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }
}
