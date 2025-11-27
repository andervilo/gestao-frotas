package dev.andervilo.gestao_frotas.application.usecase.driver;

import dev.andervilo.gestao_frotas.domain.service.DriverDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for deleting a driver.
 */
@Service
@RequiredArgsConstructor
public class DeleteDriverUseCase {
    
    private final DriverDomainService driverDomainService;
    
    @Transactional
    public void execute(UUID id) {
        driverDomainService.deleteDriver(id);
    }
}
