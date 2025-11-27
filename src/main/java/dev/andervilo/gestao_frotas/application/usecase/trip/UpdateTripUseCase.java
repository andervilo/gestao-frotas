package dev.andervilo.gestao_frotas.application.usecase.trip;

import dev.andervilo.gestao_frotas.application.dto.TripDTO;
import dev.andervilo.gestao_frotas.application.mapper.TripDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.service.TripDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for updating trips.
 */
@Service
@RequiredArgsConstructor
public class UpdateTripUseCase {
    
    private final TripDomainService tripDomainService;
    private final TripDtoMapper mapper;
    
    @Transactional
    public TripDTO completeTrip(UUID id, Long endMileage, String notes) {
        Trip trip = tripDomainService.completeTrip(id, endMileage, notes);
        return mapper.toDto(trip);
    }
}