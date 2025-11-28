package com.orioncode.shared.criteria;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CriterialParser {

    public <T> Specification<T> parse(String filter, String search) {
        Specification<T> spec = Specification.where(null);

        if (filter == null && search == null) {
            return spec;
        }

        if (filter == null || filter.isEmpty()) {
            return spec;
        }

        var filters = filter.split(";");
        var searchers = search.split(";");

        if (searchers.length != filters.length) {
            throw new InvalidFilterException("La cantidad de campos en filter y search no coinciden");
        }

        spec = spec.and((root, query, cb) -> {
            Predicate predicates = cb.conjunction();

            for (int i = 0; i < filters.length; i++) {
                var fieldName = filters[i].trim();
                var fieldValue = searchers[i].trim();

                String searchPattern = "%" + fieldValue.toLowerCase() + "%";

                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get(fieldName)), searchPattern)
                );
            }
            return predicates;
        });

        return spec;
    }

}
