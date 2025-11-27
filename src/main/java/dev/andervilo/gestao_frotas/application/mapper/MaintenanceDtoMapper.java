package dev.andervilo.gestao_frotas.application.mapper;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceDTO;
import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * MapStruct mapper for converting between MaintenanceDTO and Maintenance domain entity.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceDtoMapper {
    
    @Mapping(target = "vehicleId", source = "vehicle.id")
    MaintenanceDTO toDto(Maintenance maintenance);
    
    @Mapping(target = "vehicle", source = "vehicleId")
    Maintenance toDomain(MaintenanceDTO dto);
    
    default Vehicle mapVehicle(UUID vehicleId) {
        if (vehicleId == null) {
            return null;
        }
        return Vehicle.builder().id(vehicleId).build();
    }
}
