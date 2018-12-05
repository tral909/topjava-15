package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/ajax/profile/meals")
public class MealAjaxController extends AbstractMealController {

    @GetMapping
    public List<MealTo> get() {
        return super.getAll();
    }

    @GetMapping("/between")
    public List<MealTo> getBetween(@RequestParam(required = false) LocalTime startTime,
                               @RequestParam(required = false) LocalDate startDate,
                               @RequestParam(required = false) LocalTime endTime,
                               @RequestParam(required = false) LocalDate endDate) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @PostMapping
    public void create(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @RequestParam String description,
                       @RequestParam int calories) {
        super.create(new Meal(dateTime, description, calories));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}
