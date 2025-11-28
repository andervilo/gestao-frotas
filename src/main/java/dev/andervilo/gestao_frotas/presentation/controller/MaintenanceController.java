package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceDTO;
import dev.andervilo.gestao_frotas.application.dto.MaintenanceFilterDTO;
import dev.andervilo.gestao_frotas.application.usecase.maintenance.*;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Maintenance operations.
 */
@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Maintenance management endpoints")
public class MaintenanceController {
    
    private final CreateMaintenanceUseCase createMaintenanceUseCase;
    private final GetMaintenanceUseCase getMaintenanceUseCase;
    private final UpdateMaintenanceUseCase updateMaintenanceUseCase;
    private final DeleteMaintenanceUseCase deleteMaintenanceUseCase;
    
    @PostMapping
    @Operation(summary = "Create a new maintenance")
    public ResponseEntity<MaintenanceDTO> create(@Valid @RequestBody MaintenanceDTO dto) {
        MaintenanceDTO created = createMaintenanceUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get maintenance by ID")
    public ResponseEntity<MaintenanceDTO> getById(@PathVariable UUID id) {
        MaintenanceDTO maintenance = getMaintenanceUseCase.findById(id);
        return ResponseEntity.ok(maintenance);
    }
    
    @GetMapping
    @Operation(summary = "Get all maintenances with filters and pagination")
    public ResponseEntity<Page<MaintenanceDTO>> getAll(
            @RequestParam(required = false) UUID vehicleId,
            @RequestParam(required = false) MaintenanceType type,
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduledDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduledDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate completedDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate completedDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "scheduledDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        MaintenanceFilterDTO filter = MaintenanceFilterDTO.builder()
                .vehicleId(vehicleId)
                .type(type)
                .status(status)
                .description(description)
                .scheduledDateFrom(scheduledDateFrom)
                .scheduledDateTo(scheduledDateTo)
                .completedDateFrom(completedDateFrom)
                .completedDateTo(completedDateTo)
                .build();
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MaintenanceDTO> maintenances = getMaintenanceUseCase.findAll(filter, pageable);
        return ResponseEntity.ok(maintenances);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get maintenances by vehicle ID")
    public ResponseEntity<List<MaintenanceDTO>> getByVehicleId(@PathVariable UUID vehicleId) {
        List<MaintenanceDTO> maintenances = getMaintenanceUseCase.findByVehicleId(vehicleId);
        return ResponseEntity.ok(maintenances);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get maintenances by status")
    public ResponseEntity<List<MaintenanceDTO>> getByStatus(@PathVariable MaintenanceStatus status) {
        List<MaintenanceDTO> maintenances = getMaintenanceUseCase.findByStatus(status);
        return ResponseEntity.ok(maintenances);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get maintenances by type")
    public ResponseEntity<List<MaintenanceDTO>> getByType(@PathVariable MaintenanceType type) {
        List<MaintenanceDTO> maintenances = getMaintenanceUseCase.findByType(type);
        return ResponseEntity.ok(maintenances);
    }
    
    @PutMapping("/{id}/start")
    @Operation(summary = "Start a maintenance")
    public ResponseEntity<MaintenanceDTO> startMaintenance(@PathVariable UUID id) {
        MaintenanceDTO maintenance = updateMaintenanceUseCase.startMaintenance(id);
        return ResponseEntity.ok(maintenance);
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete a maintenance")
    public ResponseEntity<MaintenanceDTO> completeMaintenance(
            @PathVariable UUID id,
            @Parameter(description = "Final cost of the maintenance")
            @RequestParam BigDecimal finalCost,
            @Parameter(description = "Completion notes")
            @RequestParam(required = false) String notes) {
        MaintenanceDTO maintenance = updateMaintenanceUseCase.completeMaintenance(id, finalCost, notes);
        return ResponseEntity.ok(maintenance);
    }
    
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a maintenance")
    public ResponseEntity<MaintenanceDTO> cancelMaintenance(
            @PathVariable UUID id,
            @Parameter(description = "Reason for cancellation")
            @RequestParam String reason) {
        MaintenanceDTO maintenance = updateMaintenanceUseCase.cancelMaintenance(id, reason);
        return ResponseEntity.ok(maintenance);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete maintenance by ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteMaintenanceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}