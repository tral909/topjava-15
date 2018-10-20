package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MealService {
    Meal create(Meal meal);

    void delete(int id) throws NotFoundException;

    Meal get(int idMeal, int userId) throws NotFoundException;

    void update(Meal meal);

    List<Meal> getAll(int userId);

    List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate);
}