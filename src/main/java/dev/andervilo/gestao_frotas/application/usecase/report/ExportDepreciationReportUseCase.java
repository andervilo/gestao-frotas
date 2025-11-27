package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportDepreciationReportUseCase {
    
    private final ExportService exportService;
    
    public ExportDepreciationReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(DepreciationReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Marca",
            "Modelo",
            "Ano",
            "Data de Compra",
            "Valor de Compra (R$)",
            "Valor Atual (R$)",
            "Depreciação Total (R$)",
            "Taxa de Depreciação (%)",
            "Idade (anos)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleDepreciationDTO vehicle : report.vehicleDepreciation()) {
            data.add(List.of(
                vehicle.licensePlate(),
                vehicle.brand(),
                vehicle.model(),
                vehicle.year(),
                vehicle.purchaseDate() != null ? vehicle.purchaseDate().toString() : "",
                vehicle.purchaseValue(),
                vehicle.currentValue(),
                vehicle.totalDepreciation(),
                vehicle.depreciationRate(),
                vehicle.ageInYears()
            ));
        }
        
        return exportService.exportToExcel("Relatório de Depreciação de Veículos", headers, data);
    }
    
    public byte[] exportToPDF(DepreciationReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Veículo",
            "Ano",
            "Valor Compra",
            "Valor Atual",
            "Depreciação",
            "Taxa (%)"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (VehicleDepreciationDTO vehicle : report.vehicleDepreciation()) {
            String vehicleName = String.format("%s %s (%s)", 
                vehicle.brand(), vehicle.model(), vehicle.licensePlate());
            data.add(List.of(
                vehicleName,
                vehicle.year().toString(),
                String.format("R$ %.2f", vehicle.purchaseValue()),
                String.format("R$ %.2f", vehicle.currentValue()),
                String.format("R$ %.2f", vehicle.totalDepreciation()),
                String.format("%.2f%%", vehicle.depreciationRate())
            ));
        }
        
        String title = String.format("Relatório de Depreciação de Veículos\nTotal em Depreciação: R$ %.2f\nIdade Média da Frota: %d anos", 
            report.totalDepreciation(), report.averageFleetAge());
        
        return exportService.exportToPDF(title, headers, data);
    }
}
