package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

public class MealTestData {

    public static final Meal userDinner = new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal adminLunch = new Meal(100009, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);

    public static Meal getNew(){
        return new Meal(LocalDateTime.of(2021, 2, 28, 20, 30), "newMeal", 4000);
    }

    public static Meal getUpdated(){
        return new Meal(100004, LocalDateTime.of(2020, Month.JUNE, 15, 15, 15), "УжинПужин", 777);
    }

}
