package dev.andervilo.gestao_frotas.infrastructure.persistence.specification;

import dev.andervilo.gestao_frotas.application.dto.TripFilterDTO;
import dev.andervilo.gestao_frotas.infrastructure.persistence.entity.TripJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for filtering trips.
 */
public class TripSpecification {

    public static Specification<TripJpaEntity> withFilters(TripFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getVehicleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicle").get("id"), filter.getVehicleId()));
            }

            if (filter.getDriverId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("driver").get("id"), filter.getDriverId()));
            }

            if (filter.getDestination() != null && !filter.getDestination().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("destination")),
                        "%" + filter.getDestination().toLowerCase() + "%"
                ));
            }

            if (filter.getStartDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("startDateTime"), filter.getStartDateFrom()
                ));
            }

            if (filter.getStartDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("startDateTime"), filter.getStartDateTo()
                ));
            }

            if (filter.getEndDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("endDateTime"), filter.getEndDateFrom()
                ));
            }

            if (filter.getEndDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("endDateTime"), filter.getEndDateTo()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
