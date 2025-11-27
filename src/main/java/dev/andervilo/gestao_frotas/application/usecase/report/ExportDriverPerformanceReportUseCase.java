package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportDriverPerformanceReportUseCase {
    
    private final ExportService exportService;
    
    public ExportDriverPerformanceReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(DriverPerformanceReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Motorista",
            "CNH",
            "Total de Viagens",
            "Total Km",
            "Taxa de Utilização (%)",
            "Ranking"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (DriverStatsDTO driver : report.driverStats()) {
            data.add(List.of(
                driver.name(),
                driver.cnh(),
                driver.totalTrips(),
                driver.totalKm(),
                driver.utilizationRate(),
                driver.ranking()
            ));
        }
        
        return exportService.exportToExcel("Relatório de Desempenho de Motoristas", headers, data);
    }
    
    public byte[] exportToPDF(DriverPerformanceReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Motorista",
            "CNH",
            "Viagens",
            "Total Km",
            "Taxa Utilização (%)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (DriverStatsDTO driver : report.driverStats()) {
            data.add(List.of(
                driver.name(),
                driver.cnh(),
                driver.totalTrips().toString(),
                driver.totalKm().toString(),
                String.format("%.2f%%", driver.utilizationRate())
            ));
        }
        
        String title = String.format("Relatório de Desempenho de Motoristas\n%s a %s", 
            report.startDate(), report.endDate());
        
        return exportService.exportToPDF(title, headers, data);
    }
    
    public byte[] exportCNHExpiringToExcel(List<DriverCNHExpiringDTO> expiring) throws IOException {
        List<String> headers = List.of(
            "Motorista",
            "CNH",
            "Data de Vencimento",
            "Dias para Vencer",
            "Prioridade"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (DriverCNHExpiringDTO driver : expiring) {
            data.add(List.of(
                driver.name(),
                driver.cnh(),
                driver.cnhExpiration().toString(),
                driver.daysUntilExpiration(),
                driver.status()
            ));
        }
        
        return exportService.exportToExcel("CNHs a Vencer", headers, data);
    }
}
