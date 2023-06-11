package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.adminMeals.forEach(meal -> save(ADMIN_ID, meal));
        MealsUtil.userMeals.forEach(meal -> save(USER_ID, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        final Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(getNextId());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        final Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        final Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getAllByInterval(userId, null, null);
    }

    public Collection<Meal> getAllByInterval(int userId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.values().stream()
                        .filter(meal -> DateTimeUtil.isBetweenDates(meal.getDate(), startDate, endDate))
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    private int getNextId() {
        return counter.incrementAndGet();
    }
}

