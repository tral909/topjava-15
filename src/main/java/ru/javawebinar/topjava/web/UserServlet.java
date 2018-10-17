package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.util.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("set auth user id");
        String userId = req.getParameter("user_id");
        if (userId == null || userId.isEmpty()) {
            req.getRequestDispatcher("/users.jsp").forward(req, resp);
        } else {
            int id = NumberUtils.parseNumber(userId, Integer.class);
            SecurityUtil.setAuthUserId(id);
            resp.sendRedirect("meals");
        }
    }
}
