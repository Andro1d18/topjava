package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals,
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> result = new ArrayList<>();
        HashMap<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            int caloriesMealFromMap = dateCaloriesMap.getOrDefault(userMeal.getDateTime().toLocalDate(), 0);
            dateCaloriesMap.put(userMeal.getDateTime().toLocalDate(), caloriesMealFromMap + userMeal.getCalories());
        }
        for (UserMeal userMeal :
                meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                if (dateCaloriesMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay) {
                    result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                } else
                    result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        HashMap<LocalDate, Integer> dateCaloriesMap = new HashMap<>();

        meals.forEach(x -> dateCaloriesMap.put(x.getDateTime().toLocalDate(),
                dateCaloriesMap.getOrDefault(x.getDateTime().toLocalDate(), 0) + x.getCalories()));

        return meals.stream()
                .filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories()
                        , dateCaloriesMap.get(x.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
