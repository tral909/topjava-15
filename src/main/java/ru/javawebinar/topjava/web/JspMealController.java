package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.AbstractMealController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

/*    @GetMapping
    public ModelAndView get(HttpServletRequest req) {
        Meal meal = super.get(getId(req));
        return new ModelAndView("meals", "meal", meal);
    }*/

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping
    public ModelAndView getAll(HttpServletRequest req) {
        List<MealTo> list = super.getAll();
        ModelAndView mav = new ModelAndView("meals");
        mav.addObject("meals", list);
        return mav;
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update/{id}")
    public String getUpdate(@PathVariable("id") Integer id, Model model) {
        Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/create")
    public String postCreate(HttpServletRequest req) {
        super.create(buildMeal(req));
        return "redirect:/meals";
    }

    @PostMapping("/update")
    public String postUpdate(HttpServletRequest req) {
        super.update(buildMeal(req), getId(req));
        return "redirect:/meals";
    }

    @PostMapping
    public ModelAndView getBetween(HttpServletRequest req) {
        LocalDate startDate = parseLocalDate(req.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(req.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(req.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(req.getParameter("endTime"));
        List<MealTo> list = super.getBetween(startDate, startTime, endDate, endTime);
        ModelAndView mav = new ModelAndView("meals");
        mav.addObject("meals", list);
        return mav;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal buildMeal(HttpServletRequest req) {
        return new Meal(
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
    }
}
