package dev.andervilo.gestao_frotas.application.usecase.driver;

import dev.andervilo.gestao_frotas.application.dto.DriverDTO;
import dev.andervilo.gestao_frotas.application.mapper.DriverDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.service.DriverDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for updating a driver.
 */
@Service
@RequiredArgsConstructor
public class UpdateDriverUseCase {
    
    private final DriverDomainService driverDomainService;
    private final DriverDtoMapper mapper;
    
    @Transactional
    public DriverDTO execute(UUID id, DriverDTO dto) {
        Driver updated = driverDomainService.updateDriver(
                id,
                dto.getName(),
                dto.getCnhCategory(),
                dto.getCnhExpirationDate()
        );
        
        return mapper.toDto(updated);
    }
}
