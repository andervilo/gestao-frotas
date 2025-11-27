package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.TripDTO;
import dev.andervilo.gestao_frotas.application.usecase.trip.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Trip operations.
 */
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trips", description = "Trip management endpoints")
public class TripController {
    
    private final CreateTripUseCase createTripUseCase;
    private final GetTripUseCase getTripUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final DeleteTripUseCase deleteTripUseCase;
    
    @PostMapping
    @Operation(summary = "Create a new trip")
    public ResponseEntity<TripDTO> create(@Valid @RequestBody TripDTO dto) {
        TripDTO created = createTripUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get trip by ID")
    public ResponseEntity<TripDTO> getById(@PathVariable UUID id) {
        TripDTO trip = getTripUseCase.findById(id);
        return ResponseEntity.ok(trip);
    }
    
    @GetMapping
    @Operation(summary = "Get all trips")
    public ResponseEntity<List<TripDTO>> getAll() {
        List<TripDTO> trips = getTripUseCase.findAll();
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get trips by vehicle ID")
    public ResponseEntity<List<TripDTO>> getByVehicleId(@PathVariable UUID vehicleId) {
        List<TripDTO> trips = getTripUseCase.findByVehicleId(vehicleId);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get trips by driver ID")
    public ResponseEntity<List<TripDTO>> getByDriverId(@PathVariable UUID driverId) {
        List<TripDTO> trips = getTripUseCase.findByDriverId(driverId);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/in-progress")
    @Operation(summary = "Get all in-progress trips")
    public ResponseEntity<List<TripDTO>> getInProgressTrips() {
        List<TripDTO> trips = getTripUseCase.findInProgressTrips();
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/completed")
    @Operation(summary = "Get all completed trips")
    public ResponseEntity<List<TripDTO>> getCompletedTrips() {
        List<TripDTO> trips = getTripUseCase.findCompletedTrips();
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/scheduled")
    @Operation(summary = "Get all scheduled trips")
    public ResponseEntity<List<TripDTO>> getScheduledTrips() {
        List<TripDTO> trips = getTripUseCase.findScheduledTrips();
        return ResponseEntity.ok(trips);
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete a trip")
    public ResponseEntity<TripDTO> completeTrip(
            @PathVariable UUID id,
            @Parameter(description = "End mileage of the trip")
            @RequestParam Long endMileage,
            @Parameter(description = "Trip completion notes")
            @RequestParam(required = false) String notes) {
        TripDTO trip = updateTripUseCase.completeTrip(id, endMileage, notes);
        return ResponseEntity.ok(trip);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trip by ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteTripUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}