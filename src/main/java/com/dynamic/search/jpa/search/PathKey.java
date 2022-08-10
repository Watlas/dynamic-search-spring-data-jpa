package com.dynamic.search.jpa.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.Path;

/**
 * Class responsible for storing an instance of the Root {@link javax.persistence.criteria.Root} object
 * and the last name of the filtered path attribute
 */
@Getter
@RequiredArgsConstructor
public final class PathKey {


    private final Path<?> root;


    private final String lastKey;

}
