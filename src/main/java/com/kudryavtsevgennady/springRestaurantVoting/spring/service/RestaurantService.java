package com.kudryavtsevgennady.springRestaurantVoting.spring.service;

import com.kudryavtsevgennady.springRestaurantVoting.spring.model.Restaurant;
import com.kudryavtsevgennady.springRestaurantVoting.spring.repository.RestaurantRepository;
import com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception.DuplicateDataException;
import javassist.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.kudryavtsevgennady.springRestaurantVoting.spring.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAllCurrent(LocalDate date) {
        return restaurantRepository.getAllCurrent(date);
    }

    @Cacheable(value = "restaurantWithDishToday")
    public Restaurant getByIdCurrent(LocalDate date, int id) throws NotFoundException {
        return checkNotFoundWithId(restaurantRepository.getByIdCurrent(date, id), id);
    }

    public Restaurant getById(int id) throws NotFoundException {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        if (!restaurantRepository.findByName(restaurant.getName()).isEmpty()) {
            throw new DuplicateDataException("Restaurant already exists");
        }
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurantWithDishToday"}, allEntries = true)
    public Restaurant update(Restaurant restaurant) throws NotFoundException {
        Assert.notNull(restaurant, "restaurant must not be null");
        getById(restaurant.getId());
        Restaurant check = restaurantRepository.findByName(restaurant.getName()).orElse(null);
        if (check != null && check.getId() !=restaurant.getId()) {
            throw new DuplicateDataException("Restaurant with this name already exists");
        }
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    @CacheEvict(value = {"restaurants", "restaurantWithDishToday"}, allEntries = true)
    public void delete(int id) {
        restaurantRepository.deleteById(id);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }
}

