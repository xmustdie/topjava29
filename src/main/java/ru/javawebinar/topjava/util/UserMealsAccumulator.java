package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Durkin at 27.05.2023
 */

public class UserMealsAccumulator {
    private final List<UserMeal> meals = new ArrayList<>();

    private int sumCalories;

    public boolean isExcess(int caloriesPerDay) {
        return sumCalories > caloriesPerDay;
    }

    public List<UserMeal> getMeals() {
        return meals;
    }

    public void addMealWithFilter(UserMeal meal, LocalTime startTime, LocalTime endTime) {
        sumCalories += meal.getCalories();
        if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
            meals.add(meal);
        }
    }

    public UserMealsAccumulator sumAnother(UserMealsAccumulator first) {
        sumCalories += first.sumCalories;
        this.meals.addAll(first.meals);
        return this;
    }
}
