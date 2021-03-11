package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {

        User refUser = em.getReference(User.class, userId);
        if (meal.isNew()) {
            //User u = em.find(User.class, userId);
            meal.setUser(refUser);
            em.persist(meal);
            return meal;
        } else {
            Meal refMeal = em.getReference(Meal.class, meal.getId());
            if (refMeal.getUser().getId().equals(userId)) {
                meal.setUser(refUser);
                return em.merge(meal);
                //           }
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = em.getReference(Meal.class, id);
        if (meal != null && meal.getUser().getId().equals(userId)) {
            em.remove(meal);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal != null && meal.getUser().getId().equals(userId))
            return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {

        return em.createNamedQuery(Meal.GETBEETWENHALFOPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}