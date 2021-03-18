package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(entityManager.find(User.class, userId));
        if (meal.isNew())
            return crudRepository.save(meal);
        else if (get(meal.getId(), userId) == null)
            return null;
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal == null)
            return false;
        crudRepository.delete(meal);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        if (meal == null)
            return null;
        return meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Specification<Meal> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), userId);
        return crudRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Specification<Meal> specification = (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[3];
            predicates[0] = criteriaBuilder.equal(root.get("user"), userId);
            predicates[1] = criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), startDateTime);
            predicates[2] = criteriaBuilder.lessThan(root.get("dateTime"), endDateTime);
            query.where(predicates);
            return query.getRestriction();
        };
        return crudRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "dateTime"));
    }
}
