package dev.andervilo.gestao_frotas.application.usecase.report;

import dev.andervilo.gestao_frotas.application.dto.report.*;
import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class GenerateTripReportUseCase {
    
    private final ReportRepository reportRepository;
    
    public GenerateTripReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    
    public TripReportDTO execute(LocalDate startDate, LocalDate endDate) {
        List<java.util.Map<String, Object>> trips = reportRepository.getTripsByPeriod(startDate, endDate);
        List<java.util.Map<String, Object>> frequentRoutes = reportRepository.getMostFrequentRoutes(startDate, endDate, 10);
        
        List<RouteFrequencyDTO> routes = mapToRouteFrequency(frequentRoutes);
        
        // Calculate totals from actual trips
        int totalTrips = trips.size();
        int totalKm = trips.stream()
            .filter(t -> t.get("distance") != null)
            .mapToInt(t -> ((Number) t.get("distance")).intValue())
            .sum();
        double avgDistance = totalTrips > 0 ? (double) totalKm / totalTrips : 0.0;
        
        // tripsByStatus will be empty since trips table doesn't have status column
        Map<String, Integer> statusMapInt = new java.util.HashMap<>();
        
        return new TripReportDTO(
            startDate,
            endDate,
            totalTrips,
            totalKm,
            avgDistance,
            routes,
            statusMapInt
        );
    }
    
    private List<RouteFrequencyDTO> mapToRouteFrequency(List<java.util.Map<String, Object>> data) {
        return data.stream().map(row -> {
            String origin = row.get("origin") != null ? row.get("origin").toString() : "";
            String destination = row.get("destination") != null ? row.get("destination").toString() : "";
            Integer tripCount = row.get("tripcount") != null ? ((Number) row.get("tripcount")).intValue() : 0;
            Integer totalKm = row.get("totalkm") != null ? ((Number) row.get("totalkm")).intValue() : 0;
            Integer avgKm = row.get("averagekm") != null ? ((Number) row.get("averagekm")).intValue() : 0;
            
            return new RouteFrequencyDTO(
                origin,
                destination,
                tripCount,
                totalKm,
                avgKm
            );
        }).toList();
    }
}
