package dev.andervilo.gestao_frotas.presentation.controller;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.usecase.report.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints para geração de relatórios")
@CrossOrigin(origins = "*")
public class ReportController {
    
    private final GenerateCostReportUseCase generateCostReportUseCase;
    private final ExportCostReportUseCase exportCostReportUseCase;
    private final GenerateFleetUtilizationReportUseCase generateFleetUtilizationReportUseCase;
    private final ExportFleetUtilizationReportUseCase exportFleetUtilizationReportUseCase;
    private final GenerateMaintenanceReportUseCase generateMaintenanceReportUseCase;
    private final ExportMaintenanceReportUseCase exportMaintenanceReportUseCase;
    private final GenerateCorrectiveMaintenanceReportUseCase generateCorrectiveMaintenanceReportUseCase;
    private final ExportCorrectiveMaintenanceReportUseCase exportCorrectiveMaintenanceReportUseCase;
    private final GenerateDriverPerformanceReportUseCase generateDriverPerformanceReportUseCase;
    private final ExportDriverPerformanceReportUseCase exportDriverPerformanceReportUseCase;
    private final GenerateTripReportUseCase generateTripReportUseCase;
    private final ExportTripReportUseCase exportTripReportUseCase;
    private final GenerateDepreciationReportUseCase generateDepreciationReportUseCase;
    private final ExportDepreciationReportUseCase exportDepreciationReportUseCase;
    
    public ReportController(
        GenerateCostReportUseCase generateCostReportUseCase,
        ExportCostReportUseCase exportCostReportUseCase,
        GenerateFleetUtilizationReportUseCase generateFleetUtilizationReportUseCase,
        ExportFleetUtilizationReportUseCase exportFleetUtilizationReportUseCase,
        GenerateMaintenanceReportUseCase generateMaintenanceReportUseCase,
        ExportMaintenanceReportUseCase exportMaintenanceReportUseCase,
        GenerateCorrectiveMaintenanceReportUseCase generateCorrectiveMaintenanceReportUseCase,
        ExportCorrectiveMaintenanceReportUseCase exportCorrectiveMaintenanceReportUseCase,
        GenerateDriverPerformanceReportUseCase generateDriverPerformanceReportUseCase,
        ExportDriverPerformanceReportUseCase exportDriverPerformanceReportUseCase,
        GenerateTripReportUseCase generateTripReportUseCase,
        ExportTripReportUseCase exportTripReportUseCase,
        GenerateDepreciationReportUseCase generateDepreciationReportUseCase,
        ExportDepreciationReportUseCase exportDepreciationReportUseCase
    ) {
        this.generateCostReportUseCase = generateCostReportUseCase;
        this.exportCostReportUseCase = exportCostReportUseCase;
        this.generateFleetUtilizationReportUseCase = generateFleetUtilizationReportUseCase;
        this.exportFleetUtilizationReportUseCase = exportFleetUtilizationReportUseCase;
        this.generateMaintenanceReportUseCase = generateMaintenanceReportUseCase;
        this.exportMaintenanceReportUseCase = exportMaintenanceReportUseCase;
        this.generateCorrectiveMaintenanceReportUseCase = generateCorrectiveMaintenanceReportUseCase;
        this.exportCorrectiveMaintenanceReportUseCase = exportCorrectiveMaintenanceReportUseCase;
        this.generateDriverPerformanceReportUseCase = generateDriverPerformanceReportUseCase;
        this.exportDriverPerformanceReportUseCase = exportDriverPerformanceReportUseCase;
        this.generateTripReportUseCase = generateTripReportUseCase;
        this.exportTripReportUseCase = exportTripReportUseCase;
        this.generateDepreciationReportUseCase = generateDepreciationReportUseCase;
        this.exportDepreciationReportUseCase = exportDepreciationReportUseCase;
    }
    
    // ===== Relatório 1: Custos Operacionais =====
    
    @GetMapping("/costs")
    @Operation(summary = "Gerar relatório de custos operacionais (JSON)")
    public ResponseEntity<CostReportDTO> getCostReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        CostReportDTO report = generateCostReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/costs/export/excel")
    @Operation(summary = "Exportar relatório de custos para Excel")
    public ResponseEntity<byte[]> exportCostReportToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        CostReportDTO report = generateCostReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportCostReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-custos-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/costs/export/pdf")
    @Operation(summary = "Exportar relatório de custos para PDF")
    public ResponseEntity<byte[]> exportCostReportToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        CostReportDTO report = generateCostReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportCostReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-custos-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 2: Utilização de Frota =====
    
    @GetMapping("/fleet-utilization")
    @Operation(summary = "Gerar relatório de utilização de frota (JSON)")
    public ResponseEntity<FleetUtilizationReportDTO> getFleetUtilizationReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        FleetUtilizationReportDTO report = generateFleetUtilizationReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/fleet-utilization/export/excel")
    @Operation(summary = "Exportar relatório de utilização para Excel")
    public ResponseEntity<byte[]> exportFleetUtilizationToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        FleetUtilizationReportDTO report = generateFleetUtilizationReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportFleetUtilizationReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-utilizacao-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/fleet-utilization/export/pdf")
    @Operation(summary = "Exportar relatório de utilização para PDF")
    public ResponseEntity<byte[]> exportFleetUtilizationToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        FleetUtilizationReportDTO report = generateFleetUtilizationReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportFleetUtilizationReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-utilizacao-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 3: Manutenção Preventiva =====
    
    @GetMapping("/maintenance")
    @Operation(summary = "Gerar relatório de manutenção preventiva (JSON)")
    public ResponseEntity<MaintenanceReportDTO> getMaintenanceReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        MaintenanceReportDTO report = generateMaintenanceReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/maintenance/export/excel")
    @Operation(summary = "Exportar relatório de manutenção para Excel")
    public ResponseEntity<byte[]> exportMaintenanceToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        MaintenanceReportDTO report = generateMaintenanceReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportMaintenanceReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-manutencao-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/maintenance/export/pdf")
    @Operation(summary = "Exportar relatório de manutenção para PDF")
    public ResponseEntity<byte[]> exportMaintenanceToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        MaintenanceReportDTO report = generateMaintenanceReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportMaintenanceReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-manutencao-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 3.1: Manutenção Corretiva =====
    
    @GetMapping("/corrective-maintenance")
    @Operation(summary = "Gerar relatório de manutenção corretiva (JSON)")
    public ResponseEntity<CorrectiveMaintenanceReportDTO> getCorrectiveMaintenanceReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        CorrectiveMaintenanceReportDTO report = generateCorrectiveMaintenanceReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/corrective-maintenance/export/excel")
    @Operation(summary = "Exportar relatório de manutenção corretiva para Excel")
    public ResponseEntity<byte[]> exportCorrectiveMaintenanceToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        CorrectiveMaintenanceReportDTO report = generateCorrectiveMaintenanceReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportCorrectiveMaintenanceReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-manutencao-corretiva-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/corrective-maintenance/export/pdf")
    @Operation(summary = "Exportar relatório de manutenção corretiva para PDF")
    public ResponseEntity<byte[]> exportCorrectiveMaintenanceToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        CorrectiveMaintenanceReportDTO report = generateCorrectiveMaintenanceReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportCorrectiveMaintenanceReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-manutencao-corretiva-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 4: Desempenho de Motoristas =====
    
    @GetMapping("/driver-performance")
    @Operation(summary = "Gerar relatório de desempenho de motoristas (JSON)")
    public ResponseEntity<DriverPerformanceReportDTO> getDriverPerformanceReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        DriverPerformanceReportDTO report = generateDriverPerformanceReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/driver-performance/export/excel")
    @Operation(summary = "Exportar relatório de desempenho para Excel")
    public ResponseEntity<byte[]> exportDriverPerformanceToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        DriverPerformanceReportDTO report = generateDriverPerformanceReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportDriverPerformanceReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-desempenho-motoristas-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/driver-performance/export/pdf")
    @Operation(summary = "Exportar relatório de desempenho para PDF")
    public ResponseEntity<byte[]> exportDriverPerformanceToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        DriverPerformanceReportDTO report = generateDriverPerformanceReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportDriverPerformanceReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-desempenho-motoristas-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 5: Viagens =====
    
    @GetMapping("/trips")
    @Operation(summary = "Gerar relatório de viagens (JSON)")
    public ResponseEntity<TripReportDTO> getTripReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        TripReportDTO report = generateTripReportUseCase.execute(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/trips/export/excel")
    @Operation(summary = "Exportar relatório de viagens para Excel")
    public ResponseEntity<byte[]> exportTripsToExcel(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        TripReportDTO report = generateTripReportUseCase.execute(startDate, endDate);
        byte[] excelFile = exportTripReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-viagens-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/trips/export/pdf")
    @Operation(summary = "Exportar relatório de viagens para PDF")
    public ResponseEntity<byte[]> exportTripsToPDF(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws IOException {
        TripReportDTO report = generateTripReportUseCase.execute(startDate, endDate);
        byte[] pdfFile = exportTripReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-viagens-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
    
    // ===== Relatório 6: Depreciação de Veículos =====
    
    @GetMapping("/depreciation")
    @Operation(summary = "Gerar relatório de depreciação de veículos (JSON)")
    public ResponseEntity<DepreciationReportDTO> getDepreciationReport() {
        DepreciationReportDTO report = generateDepreciationReportUseCase.execute();
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/depreciation/export/excel")
    @Operation(summary = "Exportar relatório de depreciação para Excel")
    public ResponseEntity<byte[]> exportDepreciationToExcel() throws IOException {
        DepreciationReportDTO report = generateDepreciationReportUseCase.execute();
        byte[] excelFile = exportDepreciationReportUseCase.exportToExcel(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-depreciacao-" + LocalDate.now() + ".xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
    }
    
    @GetMapping("/depreciation/export/pdf")
    @Operation(summary = "Exportar relatório de depreciação para PDF")
    public ResponseEntity<byte[]> exportDepreciationToPDF() throws IOException {
        DepreciationReportDTO report = generateDepreciationReportUseCase.execute();
        byte[] pdfFile = exportDepreciationReportUseCase.exportToPDF(report);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
            "relatorio-depreciacao-" + LocalDate.now() + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfFile);
    }
}

