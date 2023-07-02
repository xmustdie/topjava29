package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 30.06.2023
 */

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() {
        Meal adminMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(adminMeal, adminMeal1);
        UserTestData.USER_MATCHER.assertMatch(adminMeal.getUser(), admin);
    }

    @Test
    public void getWithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.getWithUser(MealTestData.NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getNotOwnWithUser() {
        Assert.assertThrows(NotFoundException.class, () -> service.getWithUser(ADMIN_MEAL_ID, USER_ID));
    }
}