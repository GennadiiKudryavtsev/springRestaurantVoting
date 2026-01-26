package com.kudryavtsevgennady.springrestaurantvoting.spring.repository;

import com.kudryavtsevgennady.springrestaurantvoting.spring.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Override
    @Transactional
    Vote save(Vote vote);

    List<Vote> findByUserIdOrderByDateDesc(int userId);

    @Query("SELECT v from Vote v WHERE v.date >= :startDate AND v.date < :endDate")
    List<Vote> getAllBetweenDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.userId=:userId AND v.date >= :startDate AND v.date < :endDate")
    List<Vote> getAllBetweenDateWithUserId(@Param("userId") int userId, @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    List<Vote> getAllByRestaurantId(int restaurantId);

}
