package dev.andervilo.gestao_frotas.infrastructure.persistence.mapper;

import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.MaintenanceJpaEntity;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.VehicleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for converting between Maintenance domain entity and MaintenanceJpaEntity.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceJpaMapper {
    
    @Mapping(source = "vehicle", target = "vehicle", qualifiedByName = "vehicleToJpaEntity")
    MaintenanceJpaEntity toJpaEntity(Maintenance maintenance);
    
    @Mapping(source = "vehicle", target = "vehicle", qualifiedByName = "jpaEntityToVehicle")
    Maintenance toDomain(MaintenanceJpaEntity jpaEntity);
    
    @Named("vehicleToJpaEntity")
    default VehicleJpaEntity vehicleToJpaEntity(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleJpaEntity jpaEntity = new VehicleJpaEntity();
        jpaEntity.setId(vehicle.getId());
        return jpaEntity;
    }
    
    @Named("jpaEntityToVehicle")
    default Vehicle jpaEntityToVehicle(VehicleJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        // Return a minimal Vehicle with just the ID for the reference
        // The full Vehicle will be loaded separately if needed
        return Vehicle.builder()
                .id(jpaEntity.getId())
                .licensePlate(jpaEntity.getLicensePlate() != null ? 
                    new dev.andervilo.gestao_frotas.domain.valueobject.LicensePlate(jpaEntity.getLicensePlate()) : null)
                .type(jpaEntity.getType())
                .brand(jpaEntity.getBrand())
                .model(jpaEntity.getModel())
                .year(jpaEntity.getYear())
                .status(jpaEntity.getStatus())
                .currentMileage(jpaEntity.getCurrentMileage())
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
}
