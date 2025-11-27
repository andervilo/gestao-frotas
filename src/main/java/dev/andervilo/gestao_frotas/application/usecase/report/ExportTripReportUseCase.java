package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportTripReportUseCase {
    
    private final ExportService exportService;
    
    public ExportTripReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(TripReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Origem",
            "Destino",
            "Quantidade de Viagens",
            "Distância Total (km)",
            "Distância Média (km)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (RouteFrequencyDTO route : report.topRoutes()) {
            data.add(List.of(
                route.origin(),
                route.destination(),
                route.tripCount(),
                route.totalKm(),
                route.averageKm()
            ));
        }
        
        return exportService.exportToExcel("Relatório de Viagens", headers, data);
    }
    
    public byte[] exportToPDF(TripReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Rota",
            "Viagens",
            "Distância (km)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (RouteFrequencyDTO route : report.topRoutes()) {
            String routeName = route.origin() + " → " + route.destination();
            data.add(List.of(
                routeName,
                route.tripCount().toString(),
                String.format("%d", route.totalKm())
            ));
        }
        
        String title = String.format("Relatório de Viagens\n%s a %s\nTotal: %d viagens | Distância Média: %.1f km", 
            report.startDate(), report.endDate(), report.totalTrips(), report.averageDistance());
        
        return exportService.exportToPDF(title, headers, data);
    }
    
    public byte[] exportStatusSummaryToExcel(TripReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Status",
            "Quantidade"
        );
        
        List<List<Object>> data = new ArrayList<>();
        report.tripsByStatus().forEach((status, count) -> {
            data.add(List.of(
                translateStatus(status),
                count
            ));
        });
        
        return exportService.exportToExcel("Resumo de Status das Viagens", headers, data);
    }
    
    private String translateStatus(String status) {
        return switch (status) {
            case "COMPLETED" -> "Concluída";
            case "SCHEDULED" -> "Agendada";
            case "CANCELED" -> "Cancelada";
            case "IN_PROGRESS" -> "Em Andamento";
            default -> status;
        };
    }
}
