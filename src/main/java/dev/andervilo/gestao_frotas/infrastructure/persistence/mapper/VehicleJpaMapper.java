package dev.andervilo.gestao_frotas.infrastructure.persistence.mapper;

import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.VehicleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for converting between Vehicle domain entity and VehicleJpaEntity.
 */
@Mapper(componentModel = "spring")
public interface VehicleJpaMapper {
    
    @Mapping(source = "licensePlate", target = "licensePlate", qualifiedByName = "licensePlateToString")
    VehicleJpaEntity toJpaEntity(Vehicle vehicle);
    
    @Mapping(source = "licensePlate", target = "licensePlate", qualifiedByName = "stringToLicensePlate")
    Vehicle toDomain(VehicleJpaEntity jpaEntity);
    
    @Named("licensePlateToString")
    default String licensePlateToString(LicensePlate licensePlate) {
        return licensePlate != null ? licensePlate.getValue() : null;
    }
    
    @Named("stringToLicensePlate")
    default LicensePlate stringToLicensePlate(String licensePlate) {
        return licensePlate != null ? new LicensePlate(licensePlate) : null;
    }
}
