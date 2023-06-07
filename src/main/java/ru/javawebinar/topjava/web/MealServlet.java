package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.memory.MemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 02.06.2023
 */

public class MealServlet extends HttpServlet {
    private static final int MAX_CALORIES = 2000;
    private static final String MEALS_PAGE = "/meals.jsp";
    private static final String EDITOR_PAGE = "/mealForm.jsp";
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;

    @Override
    public void init() {
        repository = new MemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.debug("request processing by servlet: method doGet started");
        final String action = req.getParameter("action");
        int id;
        final String targetPage;
        switch (action == null ? "default" : action) {
            case "create":
                targetPage = EDITOR_PAGE;
                break;
            case "update":
                targetPage = EDITOR_PAGE;
                id = getId(req);
                logger.debug("update meal with id {}", id);
                final Meal meal = repository.get(id);
                req.setAttribute("meal", meal);
                break;
            case "delete":
                id = getId(req);
                logger.debug("delete meal with id = {}", id);
                repository.delete(id);
                resp.sendRedirect("meals");
                return;
            default:
                logger.debug("show list of meals");
                targetPage = MEALS_PAGE;
                req.setAttribute("mealTos", MealsUtil.getAllTos(repository.getAll(), MAX_CALORIES));
        }
        logger.debug("request processed by servlet: method doGet finished");
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("request processing by servlet: method doPost started");
        req.setCharacterEncoding("UTF-8");
        final String mealId = req.getParameter("id");
        final Meal meal = new Meal(mealId.isEmpty() ? null : Integer.valueOf(mealId),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
        repository.save(meal);
        logger.debug("request processed by servlet: method doPost finished");
        resp.sendRedirect("meals");
    }

    private static int getId(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }
}
