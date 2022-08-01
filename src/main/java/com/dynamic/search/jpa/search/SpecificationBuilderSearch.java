package com.dynamic.search.jpa.search;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class responsible for creating the Specification object to be sent to the repository layer
 *
 * @param <J> The type of the entity to be searched
 */
public final class SpecificationBuilderSearch<J> implements Specification<J> {

    private final Set<SearchCriteria> list;
    private final Class<J> clazz;

    public SpecificationBuilderSearch(Class<J> clazz, String search) {
        this.clazz = clazz;
        this.list = Arrays.stream(search.split(";")).map(SearchCriteria::new).collect(Collectors.toSet());
    }

    @Override
    public Predicate toPredicate(@NonNull Root<J> root, @NonNull CriteriaQuery<?> criteriaQuery, @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {

            Expression<String> expression = new FilterRoot(root, clazz).getExpression(criteria.getKey());

            switch (criteria.getOperation()) {
                case GREATER_THAN -> predicates.add(criteriaBuilder.greaterThan(
                        expression, criteria.getValue().toString()));
                case LESS_THAN -> predicates.add(criteriaBuilder.lessThan(
                        expression, criteria.getValue().toString()));
                case GREATER_THAN_EQUAL -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        expression, criteria.getValue().toString()));
                case LESS_THAN_EQUAL -> predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        expression, criteria.getValue().toString()));
                case NOT_EQUAL -> predicates.add(criteriaBuilder.notEqual(
                        expression, criteria.getValue()));
                case EQUAL -> predicates.add(criteriaBuilder.equal(
                        expression, criteria.getValue()));
                case MATCH -> predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(expression),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"));
                case MATCH_END -> predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(expression),
                        criteria.getValue().toString().toLowerCase() + "%"));
                case MATCH_START -> predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(expression),
                        "%" + criteria.getValue().toString().toLowerCase()));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
