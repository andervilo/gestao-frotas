package dev.andervilo.gestao_frotas.application.mapper;

import dev.andervilo.gestao_frotas.application.dto.DriverDTO;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between DriverDTO and Driver domain entity.
 */
@Mapper(componentModel = "spring")
public interface DriverDtoMapper {
    
    DriverDTO toDto(Driver driver);
    
    Driver toDomain(DriverDTO dto);
}
