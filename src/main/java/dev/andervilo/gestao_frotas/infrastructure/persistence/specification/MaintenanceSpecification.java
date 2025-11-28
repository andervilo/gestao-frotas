package dev.andervilo.gestao_frotas.infrastructure.persistence.specification;

import dev.andervilo.gestao_frotas.application.dto.MaintenanceFilterDTO;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.MaintenanceJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for filtering maintenances.
 */
public class MaintenanceSpecification {

    public static Specification<MaintenanceJpaEntity> withFilters(MaintenanceFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getVehicleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicle").get("id"), filter.getVehicleId()));
            }

            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + filter.getDescription().toLowerCase() + "%"
                ));
            }

            if (filter.getScheduledDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("scheduledDate"), filter.getScheduledDateFrom()
                ));
            }

            if (filter.getScheduledDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("scheduledDate"), filter.getScheduledDateTo()
                ));
            }

            if (filter.getCompletedDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("completedDate"), filter.getCompletedDateFrom()
                ));
            }

            if (filter.getCompletedDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("completedDate"), filter.getCompletedDateTo()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
