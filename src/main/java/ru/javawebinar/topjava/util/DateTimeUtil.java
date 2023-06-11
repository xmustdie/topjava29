package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || lt.compareTo(startTime) >= 0) && (endTime == null || lt.compareTo(endTime) < 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static boolean isBetweenDates(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || date.compareTo(startDate) >= 0) && (endDate == null || date.compareTo(endDate) <= 0);
    }

    public static LocalTime convertToLocaltime(String time) {
        return StringUtils.hasLength(time) ? LocalTime.parse(time) : null;
    }

    public static LocalDate convertToLocalDate(String date) {
        return StringUtils.hasLength(date) ? LocalDate.parse(date) : null;
    }
}

