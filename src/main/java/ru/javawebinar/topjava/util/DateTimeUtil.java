package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return isBetweenRightExclusive(lt, startTime, endTime);
    }

    public static boolean isBetweenDates(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return isBetweenRightExclusive(date, startDate, endDate == null ? null : endDate.plusDays(1));
    }

    public static boolean isBetweenLocalDateTimes(LocalDateTime ldt, LocalTime start, LocalTime end) {
        return isBetweenRightExclusive(ldt.toLocalTime(), start, end);
    }

    private static <T extends Comparable<T>> boolean isBetweenRightExclusive(T value, T start, T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalTime convertToLocaltime(String time) {
        return StringUtils.hasLength(time) ? LocalTime.parse(time) : null;
    }

    public static LocalDate convertToLocalDate(String date) {
        return StringUtils.hasLength(date) ? LocalDate.parse(date) : null;
    }
}

