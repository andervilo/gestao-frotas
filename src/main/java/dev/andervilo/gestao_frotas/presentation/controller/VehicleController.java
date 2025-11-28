package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.VehicleDTO;
import dev.andervilo.gestao_frotas.application.dto.VehicleFilterDTO;
import dev.andervilo.gestao_frotas.application.usecase.vehicle.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for Vehicle operations.
 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
public class VehicleController {
    
    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleUseCase getVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    
    @PostMapping
    @Operation(summary = "Create a new vehicle")
    public ResponseEntity<VehicleDTO> create(@Valid @RequestBody VehicleDTO dto) {
        VehicleDTO created = createVehicleUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<VehicleDTO> getById(@PathVariable UUID id) {
        VehicleDTO vehicle = getVehicleUseCase.findById(id);
        return ResponseEntity.ok(vehicle);
    }
    
    @GetMapping
    @Operation(summary = "Get all vehicles with optional filters and pagination")
    public ResponseEntity<Page<VehicleDTO>> getAll(
            @Parameter(description = "License plate filter (partial match)")
            @RequestParam(required = false) String licensePlate,
            
            @Parameter(description = "Brand filter (partial match)")
            @RequestParam(required = false) String brand,
            
            @Parameter(description = "Model filter (partial match)")
            @RequestParam(required = false) String model,
            
            @Parameter(description = "Year from")
            @RequestParam(required = false) Integer yearFrom,
            
            @Parameter(description = "Year to")
            @RequestParam(required = false) Integer yearTo,
            
            @PageableDefault(size = 20, sort = "licensePlate", direction = Sort.Direction.ASC) Pageable pageable) {
        
        VehicleFilterDTO filter = VehicleFilterDTO.builder()
                .licensePlate(licensePlate)
                .brand(brand)
                .model(model)
                .yearFrom(yearFrom)
                .yearTo(yearTo)
                .build();
        
        Page<VehicleDTO> vehicles = getVehicleUseCase.findAll(filter, pageable);
        return ResponseEntity.ok(vehicles);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle")
    public ResponseEntity<VehicleDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody VehicleDTO dto) {
        VehicleDTO updated = updateVehicleUseCase.execute(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteVehicleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
