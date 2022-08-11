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
final class SearchCriteria {

    /**
     * attribute name
     */
    private final String key;

    /**
     * value to search
     */
    private final Object value;

    /**
     * operation type
     */
    private final SearchOperation operation;


    public SearchCriteria(String search, Class<?> clazz) {
        operation = SearchOperation.getByString(search);
        List<String> collect = Arrays.stream(search.split(operation.getValue())).collect(Collectors.toList());
        this.key = collect.get(0);
        this.value = validAndReturnValue(Arrays.stream(collect.get(0).split("\\.")).collect(Collectors.toList()), clazz, collect.get(1));
    }


}
