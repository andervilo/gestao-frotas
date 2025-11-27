package dev.andervilo.gestao_frotas.application.usecase.trip;

import dev.andervilo.gestao_frotas.domain.service.TripDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for deleting a trip.
 */
@Service
@RequiredArgsConstructor
public class DeleteTripUseCase {
    
    private final TripDomainService tripDomainService;
    
    @Transactional
    public void execute(UUID id) {
        tripDomainService.deleteTrip(id);
    }
}