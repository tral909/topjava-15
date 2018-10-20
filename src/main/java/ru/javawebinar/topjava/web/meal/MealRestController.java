package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        List<Meal> meals = service.getAll(SecurityUtil.getAuthUserId());
        return MealsUtil.getWithExceeded(meals, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("getFiltered");
        LocalDate startDt = null;
        LocalDate endDt = null;
        LocalTime startTm = null;
        LocalTime endTm = null;

        boolean isDateValid = startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty();
        boolean isTimeValid = startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty();

        if (isDateValid) {
            try {
                startDt = LocalDate.parse(startDate);
                endDt = LocalDate.parse(endDate);
            } catch (DateTimeParseException e) {
                log.error("incorrect date format", e);
                return null;
            }
        }

        if (isTimeValid) {
            try {
                startTm = LocalTime.parse(startTime);
                endTm = LocalTime.parse(endTime);
            } catch (DateTimeParseException e) {
                log.error("incorrect time format", e);
                return null;
            }
        }

        if (!isDateValid) {
            startDt = LocalDate.MIN;
            endDt = LocalDate.MAX;
        }

        List<Meal> meals = service.getFilteredByDate(SecurityUtil.getAuthUserId(), startDt, endDt);
        int calories = SecurityUtil.authUserCaloriesPerDay();

        if (isTimeValid) {
            return MealsUtil.getFilteredWithExceeded(meals, calories, startTm, endTm);
        } else {
            return MealsUtil.getWithExceeded(meals, calories);
        }
    }

    public Meal get(int idMeal) {
        log.info("get {}", idMeal);
        return service.get(idMeal, SecurityUtil.getAuthUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) {
        if (isMyAndExist(id)) {
            log.info("delete {}", id);
            service.delete(id);
        } else {
            log.info("not exists or another user meal with id = {}", id);
        }
    }

    public void update(Meal meal, int id) {
        if (isMyAndExist(id)) {
            log.info("update {}", meal);
            service.update(meal);
        } else {
            log.info("not exists or another user meal with id = {}", id);
        }
    }

    private boolean isMyAndExist(int id) {
        return service.get(id, SecurityUtil.getAuthUserId()) != null;
    }
}