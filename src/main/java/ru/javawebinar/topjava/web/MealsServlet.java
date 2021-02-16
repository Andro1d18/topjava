package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    private final MealDaoImpl mealDao;
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_USER = "/meals.jsp";

    public MealsServlet() {
        mealDao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward="";
        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("insert")){
            forward = INSERT_OR_EDIT;
        } else if (action != null && action.equalsIgnoreCase("edit")){
            Long mealId = Long.parseLong(request.getParameter("mealId"));
            Meal meal = mealDao.getMealById(mealId);
            forward = INSERT_OR_EDIT;
            request.setAttribute("meal", meal);
        } else if (action != null && action.equalsIgnoreCase("delete")){
            Long mealId = Long.parseLong(request.getParameter("mealId"));
            mealDao.deleteMeal(mealId);
            forward = LIST_USER;
            request.setAttribute("testMealToList", mealDao.getAllMealsTo());
        } else {
            forward = LIST_USER;
            request.setAttribute("testMealToList", mealDao.getAllMealsTo());
        }

        request.getRequestDispatcher(forward).forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String mealId = request.getParameter("mealid");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("date"), dateTimeFormatter);

        if(mealId == null || mealId.isEmpty()){
            mealDao.addMeal(new Meal(localDateTime, description, calories));
        } else {
            mealDao.editMeal(mealId, new Meal(localDateTime, description, calories));
        }
        request.setAttribute("testMealToList", mealDao.getAllMealsTo());
        request.getRequestDispatcher(LIST_USER).forward(request, response);
    }
}
