package dev.andervilo.gestao_frotas.application.usecase.driver;

import dev.andervilo.gestao_frotas.application.dto.DriverDTO;
import dev.andervilo.gestao_frotas.application.dto.DriverFilterDTO;
import dev.andervilo.gestao_frotas.application.mapper.DriverDtoMapper;
import dev.andervilo.gestao_frotas.domain.service.DriverDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case for retrieving drivers.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetDriverUseCase {
    
    private final DriverDomainService driverDomainService;
    private final DriverDtoMapper mapper;
    
    public DriverDTO findById(UUID id) {
        return mapper.toDto(driverDomainService.findDriverById(id));
    }
    
    public List<DriverDTO> findAll() {
        return driverDomainService.findAllDrivers().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    public Page<DriverDTO> findAll(DriverFilterDTO filter, Pageable pageable) {
        return driverDomainService.findAllDrivers(filter, pageable)
                .map(mapper::toDto);
    }
}
