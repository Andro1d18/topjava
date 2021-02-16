package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDaoImpl implements MealDao{

    private final List<Meal> mealListDb;

    public MealDaoImpl() {
        mealListDb = new CopyOnWriteArrayList<>(MealsUtil.testMealList);
    }

    @Override
    public void addMeal(Meal meal) {
        mealListDb.add(meal);
    }

    @Override
    public void editMeal(String id, Meal meal) {
        Long targetId = Long.parseLong(id);
        for (Meal m :
                mealListDb) {
            if(m.getId().equals(targetId)){
                mealListDb.remove(m);
                mealListDb.add(meal);
            }
        }
    }

    @Override
    public void deleteMeal(Long id) {
        mealListDb.removeIf(m -> m.getId().equals(id));
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealListDb;
    }

    @Override
    public List<MealTo> getAllMealsTo() {
        return MealsUtil.convertToMealTo(mealListDb, 2000);
    }

    @Override
    public Meal getMealById(Long id) {
        for (Meal meal :
                mealListDb) {
            if (meal.getId().equals(id))
            return meal;
        }
        return null;
    }
}
