package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportCostReportUseCase {
    
    private final ExportService exportService;
    
    public ExportCostReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(CostReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Marca",
            "Modelo",
            "Custo Total (R$)",
            "Custo Manutenção (R$)",
            "KM Total",
            "Custo/KM (R$)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleCostDTO vehicle : report.vehicleCosts()) {
            data.add(List.of(
                vehicle.licensePlate(),
                vehicle.brand(),
                vehicle.model(),
                vehicle.totalCost(),
                vehicle.maintenanceCost(),
                vehicle.totalKm(),
                vehicle.costPerKm()
            ));
        }
        
        // Linha de totais
        data.add(List.of(
            "TOTAL",
            "",
            "",
            report.totalCost(),
            report.totalMaintenance(),
            "",
            report.averageCostPerKm()
        ));
        
        return exportService.exportToExcel("Relatório de Custos", headers, data);
    }
    
    public byte[] exportToPDF(CostReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Marca/Modelo",
            "Custo Total",
            "KM",
            "R$/KM"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleCostDTO vehicle : report.vehicleCosts()) {
            data.add(List.of(
                vehicle.licensePlate(),
                vehicle.brand() + " " + vehicle.model(),
                String.format("R$ %.2f", vehicle.totalCost()),
                vehicle.totalKm().toString(),
                String.format("R$ %.4f", vehicle.costPerKm())
            ));
        }
        
        String title = String.format("Relatório de Custos Operacionais\n%s a %s", 
            report.startDate(), report.endDate());
        
        return exportService.exportToPDF(title, headers, data);
    }
}
