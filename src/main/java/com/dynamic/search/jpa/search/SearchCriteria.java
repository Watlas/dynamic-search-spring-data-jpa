package com.dynamic.search.jpa.search;

import lombok.Getter;

import java.util.Objects;

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


    public SearchCriteria(String search) {
        operation = SearchOperation.getByString(search);
        Objects.requireNonNull(operation, "Invalid search operation");
        String[] split = search.split(operation.getValue());
        this.key = split[0];
        this.value = split[1];
    }


}
