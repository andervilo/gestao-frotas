package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.DashboardStatsDTO;
import dev.andervilo.gestao_frotas.application.usecase.dashboard.GetDashboardStatsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Dashboard operations.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics endpoints")
public class DashboardController {
    
    private final GetDashboardStatsUseCase getDashboardStatsUseCase;
    
    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        DashboardStatsDTO stats = getDashboardStatsUseCase.execute();
        return ResponseEntity.ok(stats);
    }
}
