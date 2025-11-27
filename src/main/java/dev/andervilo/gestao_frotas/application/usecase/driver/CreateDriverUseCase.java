package dev.andervilo.gestao_frotas.application.usecase.driver;

import dev.andervilo.gestao_frotas.application.dto.DriverDTO;
import dev.andervilo.gestao_frotas.application.mapper.DriverDtoMapper;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.service.DriverDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a new driver.
 */
@Service
@RequiredArgsConstructor
public class CreateDriverUseCase {
    
    private final DriverDomainService driverDomainService;
    private final DriverDtoMapper mapper;
    
    @Transactional
    public DriverDTO execute(DriverDTO dto) {
        Driver driver = driverDomainService.createDriver(
                dto.getName(),
                dto.getCpf(),
                dto.getCnh(),
                dto.getCnhCategory(),
                dto.getCnhExpirationDate()
        );
        
        return mapper.toDto(driver);
    }
}
