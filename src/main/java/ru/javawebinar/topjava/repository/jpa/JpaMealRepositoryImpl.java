package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
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
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User uRef = em.getReference(User.class, userId);
        meal.setUser(uRef);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            Meal mealToMerge = em.find(Meal.class, meal.getId());
            if (mealToMerge.getUser().getId() != userId) {
                return null;
            } else {
                return em.merge(meal);
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal.getUser().getId() != userId) {
            return false;
        } else {
            em.remove(meal);
            return true;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> result = em.createQuery("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .getResultList();
        return DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.BETWEEN, Meal.class)
                .setParameter(1, startDate)
                .setParameter(2, endDate)
                .setParameter(3, userId)
                .getResultList();
    }
}