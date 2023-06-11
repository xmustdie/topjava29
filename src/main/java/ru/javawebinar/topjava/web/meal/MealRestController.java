package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create meal {} for user id = {}", meal, userId);
        return service.create(userId, meal);
    }

    public void update(int id, Meal meal) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update meal {} for user id = {}", meal, userId);
        service.update(userId, meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal id = {} for user id = {}", id, userId);
        service.delete(userId, id);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal id = {} for user id = {}", id, userId);
        return service.get(userId, id);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user id = {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByDateTimeInterval(LocalDate startDate, LocalTime startTime,
                                                 LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("get in interval dates {} - {} and time {} - {}  for user {}", startDate, endDate, startTime, endTime, userId);
        return MealsUtil.getFilteredTos(service.getAllByInterval(userId, startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}