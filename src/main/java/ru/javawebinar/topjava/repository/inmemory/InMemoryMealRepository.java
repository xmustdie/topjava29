package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

@Repository
public class InMemoryMealRepository extends AbstractInMemoryRepository<Meal> implements MealRepository {
    private final Map<Integer, AbstractInMemoryRepository<Meal>> storage = new ConcurrentHashMap<>();

    {
        MealsUtil.adminMeals.forEach(meal -> save(ADMIN_ID, meal));
        MealsUtil.userMeals.forEach(meal -> save(USER_ID, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        return storage.computeIfAbsent(userId, u -> new AbstractInMemoryRepository<Meal>() {
        }).save(meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return storage.computeIfAbsent(userId, u -> new AbstractInMemoryRepository<Meal>() {
        }).delete(id);
    }

    @Override
    public Meal get(int userId, int id) {
        return storage.computeIfAbsent(userId, u -> new AbstractInMemoryRepository<Meal>() {
        }).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllByInterval(userId, null, null);
    }

    @Override
    public List<Meal> getAllByInterval(int userId, LocalDate startDate, LocalDate endDate) {
        AbstractInMemoryRepository<Meal> meals = storage.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.repository.values().stream()
                        .filter(meal -> DateTimeUtil.isBetweenDates(meal.getDate(), startDate, endDate))
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}

