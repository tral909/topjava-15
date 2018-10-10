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
import java.io.UnsupportedEncodingException;
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
    private static final String PARAM_ID = "id";
    private static final String PARAM_ACT = "action";
    private static final String OBJ = "meal";
    private static final String VIEW_PUT = "meal.jsp";
    private static final String OBJECTS = "meals";
    private static final String VIEW_ALL = "meals.jsp";
    private static final String REDIRECT = "meals";
    private static final String DATETIME = "datetime";
    private static final String DESCRIPTION = "description";
    private static final String CALORIES = "calories";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setUpEncodings(req, resp);
        String paramId = req.getParameter(PARAM_ID);
        String action = req.getParameter(PARAM_ACT);
        if (paramId != null) {
            int id;
            try {
                id = Integer.parseInt(paramId);
            } catch (NumberFormatException e) {
                log.debug("Bad id for meal: " + paramId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("<h3 style='color: red;'>Неверный параметр id</h3>");
                return;
            }
            if (action != null && action.equals("put")) {
                Meal meal = mealDao.getMeal(id);
                req.setAttribute(OBJ, meal);
                req.getRequestDispatcher(VIEW_PUT).forward(req, resp);
                log.debug("Forwarded to " + VIEW_PUT);
            } else if (action != null && action.equals("delete")) {
                mealDao.deleteMeal(id);
                resp.sendRedirect(REDIRECT);
            } else {
                log.debug("Unrecognized action parameter. Forwarded to " + VIEW_ALL);
                forwardToMeals(req, resp);
            }
        } else {
            log.debug("No id param. Forwarded to " + VIEW_ALL);
            forwardToMeals(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setUpEncodings(req, resp);
        Map<String, String[]> params = req.getParameterMap();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(params.get(DATETIME)[0], formatter);
            int calories = Integer.parseInt(params.get(CALORIES)[0]);
            Meal meal = new Meal(dateTime, params.get(DESCRIPTION)[0], calories);
            String action = req.getParameter(PARAM_ACT);
            if (action != null && action.equals("add")) {
                mealDao.addMeal(meal);
                resp.sendRedirect(REDIRECT);
                log.debug("Added new: " + meal);
            } else if (action != null && action.equals("put")) {
                int id = Integer.parseInt(params.get(PARAM_ID)[0]);
                meal.setId(id);
                mealDao.updateMeal(id, meal);
                resp.sendRedirect(REDIRECT);
                log.debug("Update meal with id = " + id + ": " + meal);
            } else {
                log.debug("Unrecognized action param. Forwarded to " + VIEW_ALL);
                forwardToMeals(req, resp);
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            params.forEach((k, v) -> sb.append(k).append("=").append(v[0]).append(", "));
            sb.subSequence(0, sb.lastIndexOf(","));
            e.printStackTrace();
            log.error("Bad POST meal parameter(s): " + sb);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("<h3 style='color: red;'>Ошибка валидации параметра(ов)</h3>");
        }
    }

    private void setUpEncodings(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
    }

    private void forwardToMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealWithExceed> meals = mealDao.getAllMeal();
        req.setAttribute(OBJECTS, meals);
        req.getRequestDispatcher(VIEW_ALL).forward(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}