package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;
    private MealRestController mealRestController;


    @Override
    public void init() {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
      //  repository = new InMemoryMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action == null ? "creteOrUpdate": "filter"){
            case "creteOrUpdate":
                String id = request.getParameter("id");

                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());

                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                if(meal.isNew()) {
                    mealRestController.create(meal);
                    response.sendRedirect("meals");
                } else {
                    mealRestController.update(meal);
                    response.sendRedirect("meals");
                }
                break;
            case "filter":

                response.sendRedirect("meals");
                break;
        }


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String startDate = request.getParameter("startDate");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
//                Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId());
//                request.setAttribute("meal", meal);
//                break;
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                 startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");

                request.setAttribute("meals",
                        MealsUtil.getFilteredByDateAndTimeTos(mealRestController.getAllMeal()
                                , MealsUtil.DEFAULT_CALORIES_PER_DAY
                        ,startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                       mealRestController.getAll());
                        // MealsUtil.getTos(repository.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        String id = request.getParameter("id");
//
//        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
//                LocalDateTime.parse(request.getParameter("dateTime")),
//                request.getParameter("description"),
//                Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());
//
//        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
//        repository.save(meal);
//        response.sendRedirect("meals");
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        switch (action == null ? "all" : action) {
//            case "delete":
//                int id = getId(request);
//                log.info("Delete {}", id);
//                repository.delete(id);
//                response.sendRedirect("meals");
//                break;
//            case "create":
//            case "update":
//                final Meal meal = "create".equals(action) ?
//                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
//                        repository.get(getId(request));
//                request.setAttribute("meal", meal);
//                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
//                break;
//            case "all":
//            default:
//                log.info("getAll");
//                request.setAttribute("meals",
//                        MealsUtil.getTos(repository.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
//                request.getRequestDispatcher("/meals.jsp").forward(request, response);
//                break;
//        }
//    }
//
//    private int getId(HttpServletRequest request) {
//        String paramId = Objects.requireNonNull(request.getParameter("id"));
//        return Integer.parseInt(paramId);
//    }
//}
