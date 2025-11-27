package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.usecase.seed.SeedMaintenanceDataUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seed")
public class SeedController {
    
    private final SeedMaintenanceDataUseCase seedMaintenanceDataUseCase;
    
    public SeedController(SeedMaintenanceDataUseCase seedMaintenanceDataUseCase) {
        this.seedMaintenanceDataUseCase = seedMaintenanceDataUseCase;
    }
    
    @PostMapping("/maintenances")
    public ResponseEntity<String> seedMaintenances() {
        String result = seedMaintenanceDataUseCase.execute();
        return ResponseEntity.ok(result);
    }
}
