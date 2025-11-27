package dev.andervilo.gestao_frotas.infrastructure.persistence.repository;

import dev.andervilo.gestao_frotas.application.dto.DriverFilterDTO;
import dev.andervilo.gestao_frotas.domain.entity.Driver;
import dev.andervilo.gestao_frotas.domain.enums.DriverStatus;
import dev.andervilo.gestao_frotas.domain.repository.DriverRepository;
import dev.andervilo.gestao_frotas.domain.specification.DriverSpecification;
import dev.andervilo.gestao_frotas.infrastructure.persistence.jpa.SpringDataDriverRepository;
import dev.andervilo.gestao_frotas.infrastructure.persistence.mapper.DriverJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of DriverRepository using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class DriverRepositoryImpl implements DriverRepository {
    
    private final SpringDataDriverRepository jpaRepository;
    private final DriverJpaMapper mapper;
    
    @Override
    public Driver save(Driver driver) {
        var jpaEntity = mapper.toJpaEntity(driver);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Driver> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Driver> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Driver> findByCnh(String cnh) {
        return jpaRepository.findByCnh(cnh)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Driver> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Driver> findAll(DriverFilterDTO filter, Pageable pageable) {
        var spec = DriverSpecification.withFilters(filter);
        return jpaRepository.findAll(spec, pageable)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Driver> findByStatus(DriverStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }
    
    @Override
    public boolean existsByCnh(String cnh) {
        return jpaRepository.existsByCnh(cnh);
    }
}
