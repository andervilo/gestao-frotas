package dev.andervilo.gestao_frotas.application.usecase.seed;

import dev.andervilo.gestao_frotas.domain.entity.Maintenance;
import dev.andervilo.gestao_frotas.domain.entity.Vehicle;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceStatus;
import dev.andervilo.gestao_frotas.domain.enums.MaintenanceType;
import dev.andervilo.gestao_frotas.domain.repository.MaintenanceRepository;
import dev.andervilo.gestao_frotas.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SeedMaintenanceDataUseCase {
    
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    
    public SeedMaintenanceDataUseCase(
            MaintenanceRepository maintenanceRepository,
            VehicleRepository vehicleRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleRepository = vehicleRepository;
    }
    
    @Transactional
    public String execute() {
        // Buscar veículos existentes
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        if (vehicles.isEmpty()) {
            return "Nenhum veículo encontrado. Por favor, cadastre veículos primeiro.";
        }
        
        // Limpar manutenções existentes (opcional)
        // maintenanceRepository.deleteAll();
        
        int count = 0;
        
        // Para cada veículo, criar algumas manutenções
        for (Vehicle vehicle : vehicles) {
            // Manutenção COMPLETED (histórico - 45 dias atrás)
            Maintenance completed1 = Maintenance.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .type(MaintenanceType.PREVENTIVE)
                .description("Troca de óleo e filtros")
                .cost(new BigDecimal("350.00"))
                .scheduledDate(LocalDateTime.now().minusDays(45))
                .startDate(LocalDateTime.now().minusDays(45))
                .completionDate(LocalDateTime.now().minusDays(44))
                .status(MaintenanceStatus.COMPLETED)
                .build();
            maintenanceRepository.save(completed1);
            count++;
            
            // Manutenção COMPLETED (histórico - 30 dias atrás)
            Maintenance completed2 = Maintenance.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .type(MaintenanceType.CORRECTIVE)
                .description("Reparo de freios")
                .cost(new BigDecimal("850.00"))
                .scheduledDate(LocalDateTime.now().minusDays(30))
                .startDate(LocalDateTime.now().minusDays(30))
                .completionDate(LocalDateTime.now().minusDays(29))
                .status(MaintenanceStatus.COMPLETED)
                .build();
            maintenanceRepository.save(completed2);
            count++;
            
            // Manutenção SCHEDULED (próxima - 15 dias)
            Maintenance scheduled1 = Maintenance.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .type(MaintenanceType.PREVENTIVE)
                .description("Revisão preventiva geral")
                .cost(new BigDecimal("500.00"))
                .scheduledDate(LocalDateTime.now().plusDays(15))
                .status(MaintenanceStatus.SCHEDULED)
                .build();
            maintenanceRepository.save(scheduled1);
            count++;
            
            // Manutenção OVERDUE (atrasada - 5 dias atrás)
            Maintenance overdue = Maintenance.builder()
                .id(UUID.randomUUID())
                .vehicle(vehicle)
                .type(MaintenanceType.PREVENTIVE)
                .description("Alinhamento e balanceamento")
                .cost(new BigDecimal("200.00"))
                .scheduledDate(LocalDateTime.now().minusDays(5))
                .status(MaintenanceStatus.SCHEDULED)
                .build();
            maintenanceRepository.save(overdue);
            count++;
        }
        
        return String.format("✅ %d manutenções criadas com sucesso para %d veículos!", 
            count, vehicles.size());
    }
}
