package dev.andervilo.gestao_frotas.infrastructure.persistence.jpa;

import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.TripJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for TripJpaEntity.
 */
@Repository
public interface SpringDataTripRepository extends JpaRepository<TripJpaEntity, UUID>,
                                                   JpaSpecificationExecutor<TripJpaEntity> {
    
    @Query("SELECT t FROM TripJpaEntity t WHERE t.vehicle.id = :vehicleId")
    List<TripJpaEntity> findByVehicleId(@Param("vehicleId") UUID vehicleId);
    
    @Query("SELECT t FROM TripJpaEntity t WHERE t.driver.id = :driverId")
    List<TripJpaEntity> findByDriverId(@Param("driverId") UUID driverId);
    
    @Query("SELECT t FROM TripJpaEntity t WHERE t.startDateTime IS NOT NULL AND t.endDateTime IS NULL")
    List<TripJpaEntity> findInProgressTrips();
    
    @Query("SELECT t FROM TripJpaEntity t WHERE t.endDateTime IS NOT NULL")
    List<TripJpaEntity> findCompletedTrips();
    
    @Query("SELECT t FROM TripJpaEntity t WHERE t.startDateTime IS NULL")
    List<TripJpaEntity> findScheduledTrips();
}
