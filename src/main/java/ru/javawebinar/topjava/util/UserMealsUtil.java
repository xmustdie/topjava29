package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesMap = new ConcurrentHashMap<>();
        for (UserMeal meal : meals) {
            final LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesMap.merge(localDate, meal.getCalories(), Integer::sum);
        }
        final List<UserMealWithExcess> mealTo = new ArrayList<>();
        for (UserMeal meal : meals) {
            final LocalDate localDate = meal.getDateTime().toLocalDate();
            final boolean isBetween = TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime);
            if (isBetween) {
                final boolean excess = caloriesMap.get(localDate) > caloriesPerDay;
                mealTo.add(new UserMealWithExcess(meal, excess));
            }
        }
        return mealTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesMap = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m, caloriesMap.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
