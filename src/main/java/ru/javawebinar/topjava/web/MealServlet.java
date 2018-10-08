package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealListDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 07.10.2018.
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealDao mealDao = new MealListDao();
    private static final String PARAM = "id";
    private static final String OBJ = "meal";
    private static final String VIEW_ONE = "meal.jsp";
    private static final String OBJECTS = "meals";
    private static final String VIEW_ALL = "meals.jsp";
    private static final String DATETIME = "datetime";
    private static final String DESCRIPTION = "description";
    private static final String CALORIES = "calories";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String param = req.getParameter(PARAM);
        if (param != null) {
            try {
                int id = Integer.parseInt(param);
                Meal meal = mealDao.getMeal(id);
                req.setAttribute(OBJ, meal);
                req.getRequestDispatcher(VIEW_ONE).forward(req, resp);
                log.debug("forward to " + VIEW_ONE);
            } catch (NumberFormatException e) {
                log.debug("Bad id for meal: " + param);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неверный параметр id");
            }
        } else {
            log.debug("forward to " + VIEW_ALL);
            renderAllMeals(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        Map<String, String[]> params = req.getParameterMap();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(params.get(DATETIME)[0], formatter);
            int calories = Integer.parseInt(params.get(CALORIES)[0]);
            Meal meal = new Meal(dateTime, params.get(DESCRIPTION)[0], calories);
            mealDao.addMeal(meal);
            renderAllMeals(req, resp);
            log.debug("Added new: " + meal);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            params.forEach((k, v) -> sb.append(k).append("=").append(v[0]).append(" "));
            log.error("Bad POST meal parameter(s): " + sb);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("<h3 style='color: red;'>Ошибка валидации параметра(ов)</h3>");
        }
    }

    private void renderAllMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealWithExceed> meals = mealDao.getAllMeal();
        req.setAttribute(OBJECTS, meals);
        req.getRequestDispatcher(VIEW_ALL).forward(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}