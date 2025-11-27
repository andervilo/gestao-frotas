package dev.andervilo.gestao_frotas.configuration;

import dev.andervilo.gestao_frotas.domain.repository.ReportRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.ReportQueriesRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataReportRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.repository.ReportRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to explicitly define the ReportRepository bean
 * to resolve Spring Boot startup dependency injection issues.
 */
@Configuration
public class ReportRepositoryConfig {
    
    @Bean
    public ReportRepository reportRepository(
        SpringDataReportRepository springDataReportRepository,
        ReportQueriesRepository reportQueriesRepository
    ) {
        return new ReportRepositoryImpl(springDataReportRepository, reportQueriesRepository);
    }
}