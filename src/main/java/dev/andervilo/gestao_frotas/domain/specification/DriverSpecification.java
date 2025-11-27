package dev.andervilo.gestao_frotas.domain.specification;

import dev.andervilo.gestao_frotas.application.dto.DriverFilterDTO;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.DriverJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for filtering drivers using Criteria API.
 */
public class DriverSpecification {
    
    public static Specification<DriverJpaEntity> withFilters(DriverFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                    )
                );
            }
            
            if (filter.getCpf() != null && !filter.getCpf().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        root.get("cpf"),
                        "%" + filter.getCpf() + "%"
                    )
                );
            }
            
            if (filter.getCnh() != null && !filter.getCnh().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        root.get("cnh"),
                        "%" + filter.getCnh() + "%"
                    )
                );
            }
            
            if (filter.getCnhCategory() != null && !filter.getCnhCategory().isBlank()) {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("cnhCategory"),
                        filter.getCnhCategory()
                    )
                );
            }
            
            if (filter.getCnhExpirationDateFrom() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("cnhExpirationDate"),
                        filter.getCnhExpirationDateFrom()
                    )
                );
            }
            
            if (filter.getCnhExpirationDateTo() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("cnhExpirationDate"),
                        filter.getCnhExpirationDateTo()
                    )
                );
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
