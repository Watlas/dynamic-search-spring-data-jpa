package com.dynamic.search.jpa.search;

import lombok.AllArgsConstructor;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * class responsible for filtering the composition path if needed and validating if the attribute exists
 */
@AllArgsConstructor
class FilterRoot<J> {

    private Path<J> root;
    private Class<J> clazz;

    public Expression<String> getExpression(String path) {
        if (!path.contains(".")) {
            return root.get(path);
        }
        List<String> list = Arrays.stream(path.split("\\.")).collect(Collectors.toList());

        existsFieldRoot(list);

        return filterListToPath(list);
    }

    /**
     * filter the list to the path
     *
     * @param list list of attributes
     * @return expression of the path
     */
    private Expression<String> filterListToPath(List<String> list) {
        Path<Object> objectPath = root.get(list.get(0));
        for (int i = 1; i < list.size() - 1; i++) {
            objectPath = objectPath.get(list.get(i));
        }
        assert objectPath != null;
        return objectPath.get(list.get(list.size() - 1));
    }

    /**
     * valid if the field exists in the class or in composition
     *
     * @param list list of fields
     */
    public void existsFieldRoot(List<String> list) {

        AtomicReference<Set<Field>> fieldsValue = new AtomicReference<>(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toSet()));

        list.forEach(e -> fieldsValue.set(Arrays.stream(fieldsValue.get().stream().
                filter(f -> f.getName().toLowerCase().equals(e.toLowerCase())).
                findFirst().
                orElseThrow(() -> new NoSuchFieldError("Invalid field path, field: " + e)).
                getType().getDeclaredFields()).collect(Collectors.toSet())));

    }

}
