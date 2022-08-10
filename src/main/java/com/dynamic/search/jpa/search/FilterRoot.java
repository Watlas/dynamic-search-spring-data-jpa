package com.dynamic.search.jpa.search;


import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * class responsible for filtering the composition path
 */
public class FilterRoot {

    private final Path<?> root;

    public FilterRoot(Path<?> root) {
        this.root = root;
    }

    /**
     * returns the path and the last key of the path
     * @param path path to be filtered
     * @return {@link PathKey}
     */
    public PathKey getPathAndLastKey(String path) {
        if (!path.contains(".")) {
            return new PathKey(root, path);
        }
        List<String> list = Arrays.stream(path.split("\\.")).collect(Collectors.toList());

        return filterListToPath(list);
    }

    /**
     * filter the list to the path
     *
     * @param list list of attributes
     * @return expression of the path
     */
    private PathKey filterListToPath(List<String> list) {
        Path<Object> objectPath = root.get(list.get(0));
        for (int i = 1; i < list.size() - 1; i++) {
            objectPath = objectPath.get(list.get(i));
        }
        return new PathKey(objectPath, CollectionUtils.lastElement(list));
    }


}
