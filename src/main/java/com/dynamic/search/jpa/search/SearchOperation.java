package com.dynamic.search.jpa.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Supported comparisons to do dynamic query
 */
@RequiredArgsConstructor
@Getter
enum SearchOperation {

    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<="),
    NOT_EQUAL("!="),
    EQUAL("=="),
    MATCH("&&"),
    MATCH_START("~&"),
    MATCH_END("&~");

    private final String value;

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
            return null;
        }
    }
}
