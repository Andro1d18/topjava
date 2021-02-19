package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll(){
        log.info("getAll");
        return service.getAllMealTo(SecurityUtil.authUserId());
    }

    public Meal get(int mealId){
        log.info("get {}", mealId);
        return service.get(mealId, SecurityUtil.authUserId());
    }
    public MealTo create(Meal meal){
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }
    public void update(Meal meal){
        log.info("update {}", meal);
        service.update(meal, SecurityUtil.authUserId());
    }
    public void delete(int mealId){
        log.info("delete {}", mealId);
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public List<Meal> getAllMeal() {
        log.info("getAll");
        return service.getAllMeal(SecurityUtil.authUserId());
    }
}