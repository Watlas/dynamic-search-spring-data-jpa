package com.dynamic.search.jpa.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.dynamic.search.jpa.search.SpecificationOperator.*;

/**
 * Supported comparisons to do dynamic query
 */
@RequiredArgsConstructor
@Getter
enum SearchOperation {

    GREATER_THAN_EQUAL(">=", ge()),
    LESS_THAN_EQUAL("<=", le()),
    LESS_THAN("<", lt()),
    GREATER_THAN(">", gt()),
    NOT_EQUAL("!=", notEq()),
    EQUAL("==", eq()),
    MATCH("&&", like()),
    MATCH_START("~&", likeStart()),
    MATCH_END("&~", likeEnd());

    private final String expression;
    private final SpecificationOperator operator;


    public static SearchOperation getByString(String value) {
        return Arrays.stream(values())
                .filter(v -> value.contains(v.getExpression()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid search operation"));

    }
}
