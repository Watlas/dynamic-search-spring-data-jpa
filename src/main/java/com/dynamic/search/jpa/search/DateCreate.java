package com.dynamic.search.jpa.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DateCreate {


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
     * Date patterns accepted when creating Query and Criteria
     *
     * @param value LocalDateTime in String
     * @return converted LocalDateTime
     */
    static LocalDateTime createLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value, getDynamicFormatLocalDateTime());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error on create LocalDateTime by String: " + value + " error:" + e.getMessage(), e);
        }
    }

    /**
     * Date patterns accepted when creating Query and Criteria
     *
     * @param value LocalDate in String
     * @return converted LocalDate
     */
    static LocalDate createLocalDate(String value) {
        try {
            return LocalDate.parse(value, getDynamicFormatLocalDateTime());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error on create LocalDate by String: " + value + " error:" + e.getMessage(), e);
        }
    }
}
