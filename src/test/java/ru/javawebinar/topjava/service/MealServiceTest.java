package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Roman on 27.10.2018.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(BREAKFAST_ID, USER_ID);
        assertMatch(meal, BREAKFAST);
    }

    @Test
    public void delete() throws Exception {
        service.delete(BREAKFAST_ID, USER_ID);
        assertMatch(service.getAll(USER_ID),
                DINNER_PLUS_1_DAY,
                LUNCH_PLUS_1_DAY,
                BREAKFAST_PLUS_1_DAY,
                DINNER,
                LUNCH
        );
    }

    @Test
    public void getBetweenDates() throws Exception {
        List<Meal> actual = service.getBetweenDates(
                LocalDate.of(2018, Month.OCTOBER, 28),
                LocalDate.of(2018, Month.OCTOBER, 29),
                USER_ID
        );
        assertMatch(actual,
                DINNER_PLUS_1_DAY,
                LUNCH_PLUS_1_DAY,
                BREAKFAST_PLUS_1_DAY,
                DINNER,
                LUNCH,
                BREAKFAST
        );
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> actual = service.getBetweenDateTimes(
                LocalDateTime.of(2018, Month.OCTOBER, 28, 9, 2),
                LocalDateTime.of(2018, Month.OCTOBER, 28, 19, 6),
                USER_ID
        );
        assertMatch(actual,
                DINNER,
                LUNCH,
                BREAKFAST
        );
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID),
                DINNER_PLUS_1_DAY,
                LUNCH_PLUS_1_DAY,
                BREAKFAST_PLUS_1_DAY,
                DINNER,
                LUNCH,
                BREAKFAST
        );
    }

    @Test
    public void update() throws Exception {
        Meal updatedMeal = new Meal(BREAKFAST);
        updatedMeal.setDescription("updates breakfast");
        updatedMeal.setCalories(1600);
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(BREAKFAST_ID, USER_ID), updatedMeal);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "test meal", 1200);
        Meal createdMeal = service.create(newMeal, USER_ID);
        newMeal.setId(createdMeal.getId());
        assertMatch(createdMeal, newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getAnothersMeal() {
        service.get(MEAL_ADM.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnothersMeal() {
        Meal updateMeal = new Meal(MEAL_ADM);
        updateMeal.setDescription("failed updating");
        updateMeal.setCalories(1111);
        service.update(updateMeal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnothersMeal() {
        service.delete(MEAL_ADM.getId(), USER_ID);
    }
}