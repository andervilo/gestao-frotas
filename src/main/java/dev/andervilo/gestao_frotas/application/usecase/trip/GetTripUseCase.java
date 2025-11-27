package dev.andervilo.gestao_frotas.application.usecase.trip;

import dev.andervilo.gestao_frotas.application.dto.TripDTO;
import dev.andervilo.gestao_frotas.application.mapper.TripDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.service.TripDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case for retrieving trips.
 */
@Service
@RequiredArgsConstructor
public class GetTripUseCase {
    
    private final TripDomainService tripDomainService;
    private final TripDtoMapper mapper;
    
    @Transactional(readOnly = true)
    public TripDTO findById(UUID id) {
        Trip trip = tripDomainService.findTripById(id);
        return mapper.toDto(trip);
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findAll() {
        List<Trip> trips = tripDomainService.findAllTrips();
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findByVehicleId(UUID vehicleId) {
        List<Trip> trips = tripDomainService.findTripsByVehicle(vehicleId);
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findByDriverId(UUID driverId) {
        List<Trip> trips = tripDomainService.findTripsByDriver(driverId);
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findInProgressTrips() {
        List<Trip> trips = tripDomainService.findInProgressTrips();
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findCompletedTrips() {
        List<Trip> trips = tripDomainService.findCompletedTrips();
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TripDTO> findScheduledTrips() {
        List<Trip> trips = tripDomainService.findScheduledTrips();
        return trips.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}