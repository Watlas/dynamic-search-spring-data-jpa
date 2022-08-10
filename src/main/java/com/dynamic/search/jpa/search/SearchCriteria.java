package com.dynamic.search.jpa.search;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dynamic.search.jpa.search.util.ValidField.validAndReturnValue;

@Getter
public final class SearchCriteria {

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



    private final Class<?> clazz ;


    public SearchCriteria(String search, Class<?> clazz) {
        operation = SearchOperation.getByString(search);
        List<String> collect = Arrays.stream(search.split(operation.getValue())).collect(Collectors.toList());
        this.key = collect.get(0);
        this.value = validAndReturnValue(Arrays.stream(collect.get(0).split("\\.")).collect(Collectors.toList()), clazz, collect.get(1));
        this.clazz = this.value.getClass();
    }


}
