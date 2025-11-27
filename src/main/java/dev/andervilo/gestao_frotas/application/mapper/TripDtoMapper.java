package dev.andervilo.gestao_frotas.application.mapper;

import dev.andervilo.gestao_frotas.application.dto.TripDTO;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * MapStruct mapper for converting between TripDTO and Trip domain entity.
 */
@Mapper(componentModel = "spring")
public interface TripDtoMapper {
    
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "driverId", source = "driver.id")
    TripDTO toDto(Trip trip);
    
    @Mapping(target = "vehicle", source = "vehicleId")
    @Mapping(target = "driver", source = "driverId")
    Trip toDomain(TripDTO dto);
    
    default Vehicle mapVehicle(UUID vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        return Vehicle.builder().id(vehicleId).build();
    }
    
    default Driver mapDriver(UUID driverId) {
        if (driverId == null) {
            return null;
        }
        return Driver.builder().id(driverId).build();
    }
}
