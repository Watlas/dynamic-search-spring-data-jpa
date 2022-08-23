package com.dynamic.search.jpa.search;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dynamic.search.jpa.search.ValidField.validAndReturnValue;

/**
 * Class responsible for storing an instance of the query operator, the path of the attribute that will be searched and
 * the object that will be compared with the values in the database
 */
@Getter
public final class SearchCriteria {

    /**
     * attribute name
     */
    private final String fieldName;

    /**
     * value to search
     */
    private final Object value;

    /**
     * operation type
     */
    private final SearchOperation operationType;

    /**
     * Constructor using for HTTP methods of type GET
     *
     * @param search search criteria
     * @param clazz  class of the entity to be searched
     */
    public SearchCriteria(String search, Class<?> clazz) {
        operationType = SearchOperation.getByString(search);
        List<String> collect = Arrays.stream(search.split(operationType.getExpression())).collect(Collectors.toList());
        this.fieldName = collect.get(0);
        this.value = validAndReturnValue(Arrays.stream(collect.get(0).split("\\.")).collect(Collectors.toList()), clazz, collect.get(1));
    }

    /**
     * Constructor using for HTTP methods of type POST
     *
     * @param fieldName attribute name
     * @param value     value to search
     * @param operation operation type
     */
    public SearchCriteria(String fieldName, String value, String operation, Class<?> clazz) {
        this.fieldName = fieldName;
        this.value = validAndReturnValue(Arrays.stream(fieldName.split("\\.")).collect(Collectors.toList()), clazz, value);
        this.operationType = SearchOperation.getByString(operation);
    }
}
