package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.util.ValidationUtil.checkPermissionAndNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkPermission;


@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public MealTo create(Meal meal, int userId) {
        checkPermission(meal, userId);
        repository.save(meal);
        List<MealTo> mealTos = getAllMealTo(userId);

        return mealTos.stream().filter(m -> m.getId().equals(meal.getUserId())).findFirst().get();
    }

    public void delete(int mealId, int userId) {
        checkPermission(repository.get(mealId), userId);
        checkNotFoundWithId(repository.delete(mealId), userId);
    }

    public Meal get(int mealId, int userId) {

        return checkPermissionAndNotFound(repository.get(mealId), mealId, userId);
    }

//    public MealTo get(int mealId, int userId) {
//        checkPermissionAndNotFound(repository.get(mealId), mealId, userId);
//        List<MealTo> mealTos = getAll(userId);
//
//        return mealTos.stream().filter(m -> m.getId() == mealId).findFirst().get();
//    }

    public List<MealTo> getAllMealTo(int userId) {
        List<Meal> meals = new ArrayList<>(repository.getAll(userId));
        return MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<Meal> getAllMeal(int userId){
        return new ArrayList<>(repository.getAll(userId));
    }

    public void update(Meal meal, int userId) {
        checkPermission(meal, userId);
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }
}