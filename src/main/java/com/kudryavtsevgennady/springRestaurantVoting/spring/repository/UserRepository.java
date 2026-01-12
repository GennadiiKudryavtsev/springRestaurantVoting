package com.kudryavtsevgennady.springRestaurantVoting.spring.repository;

import com.kudryavtsevgennady.springRestaurantVoting.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    User getByEmail(String email);

    @Override
    @Transactional
    User save(User user);

    User findById(int userId);
}