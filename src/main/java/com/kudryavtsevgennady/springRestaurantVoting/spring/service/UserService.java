package com.kudryavtsevgennady.springRestaurantVoting.spring.service;

import com.kudryavtsevgennady.springRestaurantVoting.spring.AuthorizedUser;
import com.kudryavtsevgennady.springRestaurantVoting.spring.model.User;
import com.kudryavtsevgennady.springRestaurantVoting.spring.repository.UserRepository;
import com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception.DuplicateDataException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.kudryavtsevgennady.springRestaurantVoting.spring.util.UserUtil.prepareToSave;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        if (userRepository.getByEmail(user.getEmail()) != null) {
            throw new DuplicateDataException("User with this email already exists");
        }
        return prepareAndSave(user);
    }

    private User prepareAndSave(User user) {
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    public User getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}

