package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    Meal save(Meal meal);

    Optional<Meal> findByIdAndUserId(int id, int userId);

    @Transactional
    @Modifying
    @Query(name = Meal.DELETE)
    int delete(@Param("id") int id, @Param("userId") int userId);

    List<Meal> findAllByUserId(Sort sort, int userId);

    //List<Meal> getByDateTimeBetweenOrderByDateTimeDesc(LocalDateTime startDate, LocalDateTime endDate);
    @Query(name = Meal.GET_BETWEEN)
    List<Meal> getBetween(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
