package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void update(int id, Meal meal) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        service.delete(userId, id);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        return service.get(userId, id);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(service.getAll(userId), userId);
    }

    public List<MealTo> getAllByDateTimeInterval(LocalDate startDate, LocalTime startTime,
                                                 LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getFilteredTos(service.getAllByInterval(userId, startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}