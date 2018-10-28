package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

/**
 * Created by Roman on 28.10.2018.
 */
public class MealTestData {
    public static final int BREAKFAST_ID = START_SEQ + 2;

    public static final Meal BREAKFAST = new Meal(BREAKFAST_ID, of(2018, Month.OCTOBER, 28, 9, 2), "Завтрак", 600);
    public static final Meal LUNCH = new Meal(BREAKFAST_ID + 1, of(2018, Month.OCTOBER, 28, 14, 10), "Обед", 1000);
    public static final Meal DINNER = new Meal(BREAKFAST_ID + 2, of(2018, Month.OCTOBER, 28, 19, 6), "Ужин", 400);
    public static final Meal MEAL_ADM = new Meal(BREAKFAST_ID + 3, of(2018, Month.OCTOBER, 28, 13, 13), "Еда админа", 400);
    public static final Meal MEAL_ADM_2 = new Meal(BREAKFAST_ID + 4, of(2018, Month.OCTOBER, 28, 21, 1), "Еда админа2", 1100);
    public static final Meal BREAKFAST_PLUS_1_DAY = new Meal(BREAKFAST_ID + 5, of(2018, Month.OCTOBER, 29, 9, 5), "Завтрак", 600);
    public static final Meal LUNCH_PLUS_1_DAY = new Meal(BREAKFAST_ID + 6, of(2018, Month.OCTOBER, 29, 14, 27), "Обед", 1000);
    public static final Meal DINNER_PLUS_1_DAY = new Meal(BREAKFAST_ID + 7, of(2018, Month.OCTOBER, 29, 19, 43), "Ужин", 400);
    public static final Meal MEAL_ADM_PLUS_1_DAY = new Meal(BREAKFAST_ID + 8, of(2018, Month.OCTOBER, 29, 10, 11), "Еда админа", 400);
    public static final Meal MEAL_ADM_2_PLUS_1_DAY = new Meal(BREAKFAST_ID + 9, of(2018, Month.OCTOBER, 29, 18, 55), "Еда админа2", 1100);

    public static void assertMatch(Meal actual, Meal expected) {
        //assertThat(actual).isEqualTo(expected);// - проверяется по методам equals и hashcode (в Meal они на основе id - не подходит)
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        //assertThat(actual).isEqualTo(expected);// - проверяется по методам equals и hashcode (в Meal они на основе id - не подходит)
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}