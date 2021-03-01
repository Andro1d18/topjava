package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;



    @Test
    public void get() {
        Meal mealExp = service.get(userDinner.getId(), UserTestData.USER_ID);
        Assert.assertEquals(mealExp, userDinner);

        assertThrows(EmptyResultDataAccessException.class,() -> service.get(adminLunch.getId(), UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        service.delete(userDinner.getId(),UserTestData.USER_ID);
        assertThrows(EmptyResultDataAccessException.class, () -> service.get(userDinner.getId(), UserTestData.USER_ID));

        assertThrows(NotFoundException.class,() -> service.delete(adminLunch.getId(), UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY,30),
                LocalDate.of(2020, Month.JANUARY,30),UserTestData.USER_ID);
        assertTrue(meals.contains(userDinner));
        assertFalse(meals.contains(adminLunch));

    }

    @Test
    public void getAll() {
        Assert.assertEquals(service.getAll(UserTestData.USER_ID).size(), 7);
    }


    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, UserTestData.USER_ID);
        Assert.assertEquals(service.get(userDinner.getId(),UserTestData.USER_ID), MealTestData.getUpdated());

        Assert.assertThrows(EmptyResultDataAccessException.class,() -> service.get(userDinner.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate(){
        userDinner.setId(null);
        Assert.assertThrows(DuplicateKeyException.class, () -> service.create(userDinner, UserTestData.USER_ID));
    }
    @Test
    public void create() {

        Meal created = service.create(getNew(), UserTestData.USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(created.getId());
        Assert.assertEquals(created, newMeal);
        Assert.assertEquals(service.get(created.getId(),UserTestData.USER_ID), newMeal);
    }
}