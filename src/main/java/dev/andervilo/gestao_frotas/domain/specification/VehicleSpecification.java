package dev.andervilo.gestao_frotas.domain.specification;

import dev.andervilo.gestao_frotas.application.dto.VehicleFilterDTO;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.VehicleJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for filtering vehicles using Criteria API.
 */
public class VehicleSpecification {
    
    public static Specification<VehicleJpaEntity> withFilters(VehicleFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.getLicensePlate() != null && !filter.getLicensePlate().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("licensePlate")),
                        "%" + filter.getLicensePlate().toLowerCase() + "%"
                    )
                );
            }
            
            if (filter.getBrand() != null && !filter.getBrand().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")),
                        "%" + filter.getBrand().toLowerCase() + "%"
                    )
                );
            }
            
            if (filter.getModel() != null && !filter.getModel().isBlank()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("model")),
                        "%" + filter.getModel().toLowerCase() + "%"
                    )
                );
            }
            
            if (filter.getYearFrom() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("year"),
                        filter.getYearFrom()
                    )
                );
            }
            
            if (filter.getYearTo() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("year"),
                        filter.getYearTo()
                    )
                );
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
