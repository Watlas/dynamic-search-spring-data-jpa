package com.dynamic.search.jpa.search;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * class responsible for filtering the composition path
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class FilterRoot {

    private final Path<?> root;

    private final String lastKey;

    /**
     * returns the path and the last key of the path
     *
     * @param path path to be filtered
     * @param root root of the path
     * @return {@link FilterRoot}
     */
    public static FilterRoot getPathAndLastKey(String path, Path<?> root) {
        if (!path.contains(".")) {
            return new FilterRoot(root, path);
        }
        List<String> list = Arrays.stream(path.split("\\.")).collect(Collectors.toList());

        return filterListToPath(list, root);
    }

    /**
     * filter the list to the path
     *
     * @param list list of attributes
     * @param root root of the path
     * @return expression of the path {@link FilterRoot}
     */
    private static FilterRoot filterListToPath(List<String> list, Path<?> root) {
        Path<Object> objectPath = root.get(list.get(0));
        for (int i = 1; i < list.size() - 1; i++) {
            objectPath = objectPath.get(list.get(i));
        }
        return new FilterRoot(objectPath, CollectionUtils.lastElement(list));
    }


}
