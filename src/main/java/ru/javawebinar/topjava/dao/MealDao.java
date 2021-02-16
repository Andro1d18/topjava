package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;


public interface MealDao {
    void addMeal(Meal meal);
    void editMeal(String id, Meal meal);
    void deleteMeal(Long id);
    List<Meal> getAllMeals();
    List<MealTo> getAllMealsTo();
    Meal getMealById(Long id);
}
