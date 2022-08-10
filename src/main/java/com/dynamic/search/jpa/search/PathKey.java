package com.dynamic.search.jpa.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.Path;

@Getter
@RequiredArgsConstructor
public final class PathKey {

    /**
     * Represents {@code javax.persistence.Criteria.Root}
     */
    private final Path<?> root;

    /**
     * Last key of path element
     */
    private final String lastKey;

}
