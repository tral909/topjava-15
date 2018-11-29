package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.*;

@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";

    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
                                   @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        //return super.getBetween(startDate, startTime, endDate, endTime);
        return super.getBetween(start.toLocalDate(),
                                start.toLocalTime(),
                                end.toLocalDate(),
                                end.toLocalTime());
    }
}