package com.dynamic.search.jpa.search.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidField {

    /**
     * valid field and return value
     *
     * @param list  fields path list
     * @param clazz class to search
     * @param value value to search
     * @return value to search
     */
    public static Object validAndReturnValue(List<String> list, Class<?> clazz, String value) {

        Field field = existsFieldRoot(list, clazz);

        return convertTypeDate(field, value);
    }

    /**
     * valid if the field exists in the class or in composition
     *
     * @param list list of fields
     * @return field exists
     */
    private static Field existsFieldRoot(List<String> list, Class<?> clazz) {

        final AtomicReference<List<Field>> fieldsValue = new AtomicReference<>(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));

        list.forEach(e -> fieldsValue.set(Arrays.stream(fieldsValue.get().stream().
                filter(f -> f.getName().equalsIgnoreCase(e)).
                findFirst().
                orElseThrow(() -> new NoSuchFieldError("Invalid field path, field: " + e)).
                getType().getDeclaredFields()).collect(Collectors.toList())));


        return fieldsValue.get().get(list.size() - 1);
    }


    private static Object convertTypeDate(Field field, String value) {

        if (field.getType().isAssignableFrom(LocalDate.class)) return DateUtil.createLocalDate(value);

        if (field.getType().isAssignableFrom(LocalDateTime.class)) return DateUtil.createLocalDateTime(value);

        return ObjectMapper_.INSTANCE.convert(value, field.getType());

    }

    private enum ObjectMapper_ {
        INSTANCE;
        private final ObjectMapper objectMapper;

        ObjectMapper_() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }

        /**
         * @param fromValue  json object to be deserialized to the real object type
         * @param toJavaType the real type to be converted
         */
        public Object convert(Object fromValue, Class<?> toJavaType) {
            return objectMapper.convertValue(fromValue, toJavaType);
        }
    }
}
