package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Roman on 08.10.2018.
 */
public class MealListDao implements MealDao {
    private static List<Meal> storage = new CopyOnWriteArrayList<>(MealsUtil.meals);
    private static AtomicInteger counter = new AtomicInteger(storage.size() - 1);

    @Override
    public void addMeal(Meal meal) {
        meal.setId(counter.incrementAndGet());
        storage.add(meal);
    }

    @Override
    public Meal getMeal(int id) {
        return storage.get(id);
    }

    @Override
    public List<MealWithExceed> getAllMeal() {
        return MealsUtil.getWithExceed(storage);
    }

    @Override
    public void updateMeal(int id, Meal meal) {
        storage.set(id, meal);
    }

    @Override
    public void deleteMeal(int id) {
        storage.remove(id);
    }
}