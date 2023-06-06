package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 02.06.2023
 */

public interface MealRepository {

    List<Meal> getAll();

    Meal get(int id);

    void delete(int id);

    Meal save(Meal meal);
}