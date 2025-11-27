package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.application.service.ExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportCorrectiveMaintenanceReportUseCase {
    
    private final ExportService exportService;
    
    public ExportCorrectiveMaintenanceReportUseCase(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public byte[] exportToExcel(CorrectiveMaintenanceReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Tipo",
            "Descrição",
            "Data Agendada",
            "Data Conclusão",
            "Custo (R$)",
            "Status"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (MaintenanceHistoryDTO maintenance : report.correctiveHistory()) {
            data.add(List.of(
                maintenance.licensePlate(),
                maintenance.type(),
                maintenance.description(),
                maintenance.scheduledDate() != null ? maintenance.scheduledDate().toString() : "",
                maintenance.completionDate() != null ? maintenance.completionDate().toString() : "",
                maintenance.cost(),
                maintenance.status()
            ));
        }
        
        return exportService.exportToExcel("Relatório de Manutenção Corretiva", headers, data);
    }
    
    public byte[] exportToPDF(CorrectiveMaintenanceReportDTO report) throws IOException {
        List<String> headers = List.of(
            "Placa",
            "Tipo",
            "Data Agendada",
            "Custo",
            "Status"
        );
        
        List<List<Object>> data = new ArrayList<>();
        for (MaintenanceHistoryDTO maintenance : report.correctiveHistory()) {
            data.add(List.of(
                maintenance.licensePlate(),
                maintenance.type(),
                maintenance.scheduledDate() != null ? maintenance.scheduledDate().toString() : "",
                String.format("R$ %.2f", maintenance.cost()),
                maintenance.status()
            ));
        }
        
        String title = String.format("Relatório de Manutenção Corretiva\n%s a %s", 
            report.startDate(), report.endDate());
        
        return exportService.exportToPDF(title, headers, data);
    }
}
