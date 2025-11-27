package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportFleetUtilizationReportUseCase {
    
    private final ExportService exportService;
    
    public ExportFleetUtilizationReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(FleetUtilizationReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Marca",
            "Modelo",
            "Total Viagens",
            "KM Total",
            "Dias em Uso",
            "Dias Parado",
            "Taxa Utilização (%)",
            "Status"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleUtilizationDTO vehicle : report.vehicleUtilization()) {
            data.add(List.of(
                vehicle.licensePlate(),
                vehicle.brand(),
                vehicle.model(),
                vehicle.totalTrips(),
                vehicle.totalKm(),
                vehicle.daysInUse(),
                vehicle.daysIdle(),
                vehicle.utilizationRate(),
                vehicle.utilizationStatus()
            ));
        }
        
        return exportService.exportToExcel("Utilização de Frota", headers, data);
    }
    
    public byte[] exportToPDF(FleetUtilizationReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Marca/Modelo",
            "Viagens",
            "Utilização %",
            "Status"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleUtilizationDTO vehicle : report.vehicleUtilization()) {
            data.add(List.of(
                vehicle.licensePlate(),
                vehicle.brand() + " " + vehicle.model(),
                vehicle.totalTrips().toString(),
                String.format("%.1f%%", vehicle.utilizationRate()),
                vehicle.utilizationStatus()
            ));
        }
        
        String title = String.format("Relatório de Utilização de Frota\n%s a %s", 
            report.startDate(), report.endDate());
        
        return exportService.exportToPDF(title, headers, data);
    }
}
