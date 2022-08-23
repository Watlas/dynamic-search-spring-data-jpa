package com.dynamic.search.jpa.search;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dynamic.search.jpa.search.FilterRoot.getPathAndLastKey;

/**
 * Class responsible for creating the Specification object to be sent to the repository layer
 *
 * @param <J> The type of the entity to be searched
 */
public final class SpecificationBuilderSearch<J> implements Specification<J> {

    /**
     * The list of search criteria
     */
    private List<SearchCriteria> list = new ArrayList<>();

    /**
     * The class of the entity to be searched, used on requisition HTTP GET
     *
     * @param clazz  The type of the entity to be searched
     * @param search The search criteria, example <p>name==John;age>=30;dateLastModified<=2020-01-01</p>
     */
    public SpecificationBuilderSearch(Class<J> clazz, String search) {
        this.list = Arrays.stream(search.split(";")).map((String search1) -> new SearchCriteria(search1, clazz)).collect(Collectors.toList());
    }
    /**
     * The class of the entity to be searched, used on requisition HTTP POST
     *
     * @param clazz  The type of the entity to be searched
     * @param search The search criteria, example:
     * [
     *  {
     *     fieldName: "name",
     *     operationType: "==",
     *     value: "EUA"
     *  },
     *  {
     *     fieldName: "state.name",
     *     operationType: "==",
     *     value: "SP"
     *  }
     *]
     */
    public SpecificationBuilderSearch(Class<J> clazz, JsonNode search) {
        search.forEach(jsonNode -> list.add(new SearchCriteria(jsonNode.get("fieldName").asText(), jsonNode.get("value").asText(), jsonNode.get("operationType").asText(), clazz)));
    }


    /**
     * Creates the Specification object to be sent to the repository layer
     *
     * @param root            {@link Root}
     * @param criteriaQuery   {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Specification}
     */
    @Override
    public Predicate toPredicate(@NonNull Root<J> root, @NonNull CriteriaQuery<?> criteriaQuery, @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {

            final Comparable<?> convertedValue = getConvertedValue(criteria.getValue());

            FilterRoot pathKey = getPathAndLastKey(criteria.getFieldName(), root);

            Predicate apply = criteria.getOperationType().getOperator().apply(pathKey.getRoot(), criteriaBuilder, pathKey.getLastKey(), convertedValue);

            predicates.add(apply);

        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    /**
     * @param value serialized value of the real object
     * @return {@code Comparable<?>}
     */
    private Comparable<?> getConvertedValue(Object value) {
        return new Comparable[]{(Comparable<?>) value}[0];
    }


}
