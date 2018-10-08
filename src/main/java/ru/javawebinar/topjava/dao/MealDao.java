package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

/**
 * Created by Roman on 08.10.2018.
 */
public interface MealDao {
    void addMeal(Meal meal);

    Meal getMeal(int id);

    List<MealWithExceed> getAllMeal();

    void updateMeal(int id, Meal meal);

    void deleteMeal(int id);
}