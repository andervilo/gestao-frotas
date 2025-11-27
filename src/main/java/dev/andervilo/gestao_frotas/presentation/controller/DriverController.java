package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.DriverDTO;
import dev.andervilo.gestao_frotas.application.dto.DriverFilterDTO;
import dev.andervilo.gestao_frotas.application.usecase.driver.CreateDriverUseCase;
import dev.andervilo.gestao_frotas.application.usecase.driver.DeleteDriverUseCase;
import dev.andervilo.gestao_frotas.application.usecase.driver.GetDriverUseCase;
import dev.andervilo.gestao_frotas.application.usecase.driver.UpdateDriverUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * REST controller for Driver operations.
 */
@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Driver management endpoints")
public class DriverController {
    
    private final CreateDriverUseCase createDriverUseCase;
    private final GetDriverUseCase getDriverUseCase;
    private final UpdateDriverUseCase updateDriverUseCase;
    private final DeleteDriverUseCase deleteDriverUseCase;
    
    @PostMapping
    @Operation(summary = "Create a new driver")
    public ResponseEntity<DriverDTO> create(@Valid @RequestBody DriverDTO dto) {
        DriverDTO created = createDriverUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<DriverDTO> getById(@PathVariable UUID id) {
        DriverDTO driver = getDriverUseCase.findById(id);
        return ResponseEntity.ok(driver);
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers with optional filters and pagination")
    public ResponseEntity<Page<DriverDTO>> getAll(
            @Parameter(description = "Driver name filter (partial match)") 
            @RequestParam(required = false) String name,
            
            @Parameter(description = "CPF filter (partial match)")
            @RequestParam(required = false) String cpf,
            
            @Parameter(description = "CNH filter (partial match)")
            @RequestParam(required = false) String cnh,
            
            @Parameter(description = "CNH category filter (exact match)")
            @RequestParam(required = false) String cnhCategory,
            
            @Parameter(description = "CNH expiration date from (format: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cnhExpirationDateFrom,
            
            @Parameter(description = "CNH expiration date to (format: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cnhExpirationDateTo,
            
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        
        DriverFilterDTO filter = DriverFilterDTO.builder()
                .name(name)
                .cpf(cpf)
                .cnh(cnh)
                .cnhCategory(cnhCategory)
                .cnhExpirationDateFrom(cnhExpirationDateFrom)
                .cnhExpirationDateTo(cnhExpirationDateTo)
                .build();
        
        Page<DriverDTO> drivers = getDriverUseCase.findAll(filter, pageable);
        return ResponseEntity.ok(drivers);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a driver")
    public ResponseEntity<DriverDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody DriverDTO dto) {
        DriverDTO updated = updateDriverUseCase.execute(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a driver")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteDriverUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
