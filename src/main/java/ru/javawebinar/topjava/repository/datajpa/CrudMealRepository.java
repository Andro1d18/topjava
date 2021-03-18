package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.javawebinar.topjava.model.Meal;

public interface CrudMealRepository extends JpaRepository<Meal, Integer>, JpaSpecificationExecutor<Meal> {
}
