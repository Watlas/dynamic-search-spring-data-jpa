package com.dynamic.search.jpa.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.dynamic.search.jpa.search.SpecificationOperator.*;

/**
 * Supported comparisons to do dynamic query
 */
@RequiredArgsConstructor
@Getter
public enum SearchOperation {

    GREATER_THAN(">", gt()),
    LESS_THAN("<", lt()),
    GREATER_THAN_EQUAL(">=", ge()),
    LESS_THAN_EQUAL("<=", le()),
    NOT_EQUAL("!=", notEq()),
    EQUAL("==", eq()),
    MATCH("&&", like()),
    MATCH_START("~&", likeStart()),
    MATCH_END("&~", likeEnd());

    private final String value;
    private final SpecificationOperator operator;


    public static SearchOperation getByString(String value) {

        if (value.contains("<=")) {
            return LESS_THAN_EQUAL;
        } else if (value.contains(">=")) {
            return GREATER_THAN_EQUAL;
        } else if (value.contains("<")) {
            return LESS_THAN;
        } else if (value.contains(">")) {
            return GREATER_THAN;
        } else if (value.contains("!=")) {
            return NOT_EQUAL;
        } else if (value.contains("==")) {
            return EQUAL;
        } else if (value.contains("&&")) {
            return MATCH;
        } else if (value.contains("~&")) {
            return MATCH_START;
        } else if (value.contains("&~")) {
            return MATCH_END;
        } else {
            throw new IllegalArgumentException("Invalid search operation");
        }
    }
}
