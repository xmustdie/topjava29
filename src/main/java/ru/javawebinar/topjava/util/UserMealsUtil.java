package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
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
        System.out.println();
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        System.out.println(filteredByLoopsOnePass(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        System.out.println(filteredByStreamOnPass(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        System.out.println(filteredByStreamOnPassWithCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesMap = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> mealTos = new ArrayList<>();
        for (UserMeal meal : meals) {
            boolean isBetween = TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
            if (isBetween) {
                boolean excess = caloriesMap.get(meal.getDate()) > caloriesPerDay;
                mealTos.add(convertToMealWithExcess(meal, excess));
            }
        }
        return mealTos;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesMap = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(m -> convertToMealWithExcess(m, caloriesMap.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByLoopsOnePass(List<UserMeal> meals, LocalTime startTime,
                                                                  LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, UserMealsAccumulator> groupedMeals = new HashMap<>();
        for (UserMeal userMeal : meals) {
            UserMealsAccumulator mealsAccumulator = groupedMeals.get(userMeal.getDate());
            if (mealsAccumulator == null) {
                mealsAccumulator = new UserMealsAccumulator();
                groupedMeals.put(userMeal.getDate(), mealsAccumulator);
            }
            mealsAccumulator.addMealWithFilter(userMeal, startTime, endTime);
        }
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        for (UserMealsAccumulator mealsAccumulator : groupedMeals.values()) {
            for (UserMeal meal : mealsAccumulator.getMeals()) {
                mealsTo.add(convertToMealWithExcess(meal, mealsAccumulator.isExcess(caloriesPerDay)));
            }
        }
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreamOnPass(List<UserMeal> meals, LocalTime startTime,
                                                                  LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate))
                .values().stream()
                .flatMap(group -> {
                    boolean excess = group.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return group.stream()
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                            .map(meal -> convertToMealWithExcess(meal, excess));
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamOnPassWithCustomCollector(List<UserMeal> meals, LocalTime startTime,
                                                                                     LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collector.of(UserMealsAccumulator::new,
                        ((mealsAccumulator, userMeal) -> mealsAccumulator.addMealWithFilter(userMeal, startTime, endTime)),
                        (UserMealsAccumulator::sumAnother),
                        mealsAccumulator -> mealsAccumulator.getMeals().stream().map(meal -> convertToMealWithExcess(meal,
                                mealsAccumulator.isExcess(caloriesPerDay)))))).values()
                .stream()
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess convertToMealWithExcess(UserMeal userMeal, boolean excess) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
    }

}
