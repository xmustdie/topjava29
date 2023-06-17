package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 17.06.2023
 */
@ContextConfiguration({
        "classpath:spring/spring-inmemory.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {
    @Autowired
    private MealService service;
    @Autowired
    private InMemoryMealRepository repository;

    @Before
    public void setUp() {
        repository.init();
    }

    @Test
    public void get() {
        Meal meal = service.get(MealTestData.MEAL1_ID, USER_ID);
        assertMatch(meal, MealTestData.meal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotYourOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotYourOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 31),
                        LocalDate.of(2020, Month.JANUARY, 31), USER_ID),
                meal7, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenNullDates() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), meals);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void update() {
        Meal updated = getForUpdate();
        service.update(updated, USER_ID);
        assertMatch(updated, service.get(updated.getId(), USER_ID));
    }

    @Test
    public void updateNotYourOwn() {
        Meal updated = getForUpdate();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    private Meal getForUpdate() {
        return new Meal(MEAL1_ID, meal1.getDateTime().plusHours(2), "Обновленный завтрак", meal1.getCalories() * 2);
    }

    @Test
    public void create() {
        Meal created = service.create(getNewMeal(), USER_ID);
        int newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    private Meal getNewMeal() {
        return new Meal(null, of(2020, Month.FEBRUARY, 1, 7, 0), "Новый завтрак", 800);
    }
}