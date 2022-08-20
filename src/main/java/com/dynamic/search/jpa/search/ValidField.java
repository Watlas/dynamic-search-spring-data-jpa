package com.dynamic.search.jpa.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.*;
import static java.util.Arrays.stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class ValidField {

    /**
     * valid field and return value
     *
     * @param list  fields path list
     * @param clazz class to search
     * @param value value to search
     * @return value to search
     */
    public static Object validAndReturnValue(List<String> list, Class<?> clazz, String value) {

        Class<?> classField = existsFieldRoot(list, clazz);

        return convertType(classField, value);
    }

    /**
     * valid if the field exists in the class or in composition
     *
     * @param list list of fields
     * @return field exists
     */
    private static Class<?> existsFieldRoot(List<String> list, Class<?> clazz) {

        final AtomicReference<List<Field>> fieldsValue = new AtomicReference<>(stream(clazz.getDeclaredFields()).collect(Collectors.toList()));

        Field fieldReturn = fieldsValue.get().get(0);

        for (String e : list) {

            Field field = fieldsValue.get().stream().
                    filter(f -> f.getName().equalsIgnoreCase(e)).
                    findFirst().
                    orElseThrow(() -> new NoSuchFieldError("Invalid field path, field: " + e));

            fieldsValue.set(stream(field.getType().getDeclaredFields()).collect(Collectors.toList()));

            fieldReturn = field;
        }


        return fieldReturn.getType();
    }

    /**
     * convert type of the field
     *
     * @param field field to convert
     * @param value value to convert
     * @return converted value
     */
    private static Object convertType(Class<?> field, String value) {
        return ObjectMapper_.INSTANCE.convert(value, field);

    }

    /**
     * Date patterns accepted when creating Query and Criteria
     * yyyy/MM/dd HH:mm:ss.SSSSSS
     * yyyy-MM-dd HH:mm:ss
     * ddMMMyyyy:HH:mm:ss.SSS
     * yyyy-MM-dd HH:mm
     * yyyy-MM-dd
     * yyyy/MM/dd HH:mm
     * yyyy/MM/dd
     *
     * @return A DateTimeFormatter in the accepted standard
     */
    private static DateTimeFormatter getDynamicFormatLocalDateTime() {
        return new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("[yyyy/MM/dd HH:mm:ss.SSSSSS]"))
                .appendOptional(DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss[.SSS]]"))
                .appendOptional(DateTimeFormatter.ofPattern("[ddMMMyyyy:HH:mm:ss.SSS[ Z]]"))
                .appendOptional(new DateTimeFormatterBuilder()
                        .appendPattern("[yyyy-MM-dd [HH:mm]]")
                        .parseDefaulting(HOUR_OF_DAY, 0)
                        .parseDefaulting(MINUTE_OF_HOUR, 0)
                        .parseDefaulting(SECOND_OF_MINUTE, 0)
                        .parseDefaulting(MILLI_OF_SECOND, 0)
                        .toFormatter())
                .appendOptional(new DateTimeFormatterBuilder()
                        .appendPattern("[yyyy-MM-dd]")
                        .parseDefaulting(SECOND_OF_MINUTE, 0)
                        .parseDefaulting(MILLI_OF_SECOND, 0)
                        .toFormatter())
                .appendOptional(new DateTimeFormatterBuilder()
                        .appendPattern("[yyyy/MM/dd [HH:mm]]")
                        .parseDefaulting(HOUR_OF_DAY, 0)
                        .parseDefaulting(MINUTE_OF_HOUR, 0)
                        .parseDefaulting(SECOND_OF_MINUTE, 0)
                        .parseDefaulting(MILLI_OF_SECOND, 0)
                        .toFormatter())
                .appendOptional(new DateTimeFormatterBuilder()
                        .appendPattern("[yyyy/MM/dd]")
                        .parseDefaulting(SECOND_OF_MINUTE, 0)
                        .parseDefaulting(MILLI_OF_SECOND, 0)
                        .toFormatter())
                .toFormatter();
    }


    /**
     * Object mapper to convert types
     */
    private enum ObjectMapper_ {
        INSTANCE;
        private final ObjectMapper objectMapper;

        ObjectMapper_() {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(getDynamicFormatLocalDateTime()));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(getDynamicFormatLocalDateTime()));
            objectMapper = new ObjectMapper().registerModule(javaTimeModule);
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
