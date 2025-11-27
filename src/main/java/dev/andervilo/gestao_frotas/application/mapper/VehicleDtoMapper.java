package dev.andervilo.gestao_frotas.application.mapper;

import dev.andervilo.gestao_frotas.application.dto.VehicleDTO;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for converting between VehicleDTO and Vehicle domain entity.
 */
@Mapper(componentModel = "spring")
public interface VehicleDtoMapper {
    
    @Mapping(source = "licensePlate", target = "licensePlate", qualifiedByName = "licensePlateToString")
    VehicleDTO toDto(Vehicle vehicle);
    
    @Mapping(source = "licensePlate", target = "licensePlate", qualifiedByName = "stringToLicensePlate")
    Vehicle toDomain(VehicleDTO dto);
    
    @Named("licensePlateToString")
    default String licensePlateToString(LicensePlate licensePlate) {
        return licensePlate != null ? licensePlate.getValue() : null;
    }
    
    @Named("stringToLicensePlate")
    default LicensePlate stringToLicensePlate(String licensePlate) {
        return licensePlate != null ? new LicensePlate(licensePlate) : null;
    }
}
