package dev.andervilo.gestao_frotas.infrastructure.persistence.mapper;

import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.DriverJpaEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Driver domain entity and DriverJpaEntity.
 */
@Mapper(componentModel = "spring")
public interface DriverJpaMapper {
    
    DriverJpaEntity toJpaEntity(Driver driver);
    
    Driver toDomain(DriverJpaEntity jpaEntity);
}
