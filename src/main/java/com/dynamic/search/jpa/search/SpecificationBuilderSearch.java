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

    /**
     * The list of search criteria
     */
    private final Set<SearchCriteria> list;

    /**
     * The class of the entity to be searched
     *
     * @param clazz The type of the entity to be searched
     * @param search The search criteria, example <p>"name==John;age>=30;dateLastModified<=2020-01-01" </p>
     */
    public SpecificationBuilderSearch(Class<J> clazz, String search) {
        this.list = Arrays.stream(search.split(";")).map((String search1) -> new SearchCriteria(search1, clazz)).collect(Collectors.toSet());
    }

    /**
     * Creates the Specification object to be sent to the repository layer
     * @param root  {@link Root}
     * @param criteriaQuery {@link CriteriaQuery}
     * @param criteriaBuilder  {@link CriteriaBuilder}
     * @return {@link Specification}
     */
    @Override
    public Predicate toPredicate(@NonNull Root<J> root, @NonNull CriteriaQuery<?> criteriaQuery, @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {

            final Comparable<?>[] convertedValues = getConvertedValue(criteria.getClazz(), criteria.getValue());

            PathKey pathKey = new FilterRoot(root).getPathAndLastKey(criteria.getKey());

            Predicate apply = criteria.getOperation().getOperator().apply(pathKey.getRoot(), criteriaBuilder, pathKey.getLastKey(), convertedValues);

            predicates.add(apply);

        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    /**
     * Deserializes then given {@code value} array back to real object using {@code javaType}
     *
     * @param javaType real type of the object
     * @param value    serialized value of the real object
     * @return {@code Comparable<?>[]}
     */
    private Comparable<?>[] getConvertedValue(Class<?> javaType, Object value) {
        return new Comparable[]{(Comparable) value};
    }


}
