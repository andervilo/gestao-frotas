package dev.andervilo.gestao_frotas.application.usecase.trip;

import dev.andervilo.gestao_frotas.application.dto.TripDTO;
import dev.andervilo.gestao_frotas.application.mapper.TripDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.service.TripDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new trip.
 */
@Service
@RequiredArgsConstructor
public class CreateTripUseCase {
    
    private final TripDomainService tripDomainService;
    private final TripDtoMapper mapper;
    
    @Transactional
    public TripDTO execute(TripDTO dto) {
        Trip trip = tripDomainService.createTrip(
                dto.getVehicleId(),
                dto.getDriverId(),
                dto.getOrigin(),
                dto.getDestination(),
                dto.getStartMileage()
        );
        
        return mapper.toDto(trip);
    }
}