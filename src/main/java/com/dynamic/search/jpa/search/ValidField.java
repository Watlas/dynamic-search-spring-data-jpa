package com.dynamic.search.jpa.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

        Field field = existsFieldRoot(list, clazz);

        return convertType(field, value);
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

    /**
     * convert type of the field
     *
     * @param field field to convert
     * @param value value to convert
     * @return converted value
     */
    private static Object convertType(Field field, String value) {
        return ObjectMapper_.INSTANCE.convert(value, field.getType());

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
            LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(getDynamicFormatLocalDateTime());
            LocalDateDeserializer dateDeserializer = new LocalDateDeserializer(getDynamicFormatLocalDateTime());
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
            javaTimeModule.addDeserializer(LocalDate.class, dateDeserializer);

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
