package dev.andervilo.gestao_frotas.infrastructure.persistence.mapper;

import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.entity.Trip;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.DriverJpaEntity;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.TripJpaEntity;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.VehicleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for converting between Trip domain entity and TripJpaEntity.
 */
@Mapper(componentModel = "spring")
public interface TripJpaMapper {
    
    @Mapping(source = "vehicle", target = "vehicle", qualifiedByName = "vehicleToJpaEntity")
    @Mapping(source = "driver", target = "driver", qualifiedByName = "driverToJpaEntity")
    TripJpaEntity toJpaEntity(Trip trip);
    
    @Mapping(source = "vehicle", target = "vehicle", qualifiedByName = "jpaEntityToVehicle")
    @Mapping(source = "driver", target = "driver", qualifiedByName = "jpaEntityToDriver")
    Trip toDomain(TripJpaEntity jpaEntity);
    
    @Named("vehicleToJpaEntity")
    default VehicleJpaEntity vehicleToJpaEntity(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleJpaEntity jpaEntity = new VehicleJpaEntity();
        jpaEntity.setId(vehicle.getId());
        return jpaEntity;
    }
    
    @Named("driverToJpaEntity")
    default DriverJpaEntity driverToJpaEntity(Driver driver) {
        if (driver == null) {
            return null;
        }
        DriverJpaEntity jpaEntity = new DriverJpaEntity();
        jpaEntity.setId(driver.getId());
        return jpaEntity;
    }
    
    @Named("jpaEntityToVehicle")
    default Vehicle jpaEntityToVehicle(VehicleJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
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
    
    @Named("jpaEntityToDriver")
    default Driver jpaEntityToDriver(DriverJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return Driver.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .cpf(jpaEntity.getCpf())
                .cnh(jpaEntity.getCnh())
                .cnhCategory(jpaEntity.getCnhCategory())
                .cnhExpirationDate(jpaEntity.getCnhExpirationDate())
                .status(jpaEntity.getStatus())
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
}
