package com.kudryavtsevgennady.springRestaurantVoting.spring.service;

import com.kudryavtsevgennady.springRestaurantVoting.spring.model.Dish;
import com.kudryavtsevgennady.springRestaurantVoting.spring.model.Restaurant;
import com.kudryavtsevgennady.springRestaurantVoting.spring.repository.DishRepository;
import com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception.DuplicateDataException;
import javassist.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.kudryavtsevgennady.springRestaurantVoting.spring.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final RestaurantService restaurantService;

    public DishService(DishRepository dishRepository, RestaurantService restaurantService) {
        this.dishRepository = dishRepository;
        this.restaurantService = restaurantService;
    }

    public Dish get(int id) throws NotFoundException {
        return checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
    }

    public List<Dish> getAll(int restaurantId) throws NotFoundException {
        restaurantService.getById(restaurantId);
        return checkNotFoundWithId(dishRepository.getAll(restaurantId), restaurantId);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurantWithDishToday"}, allEntries = true)
    public Dish create(Dish dish, int restaurantId) throws NotFoundException {
        Assert.notNull(dish, "dish must not be null");
        Restaurant restaurant = restaurantService.getById(restaurantId);
        dish.setRestaurant(restaurant);
        dish.setDate(LocalDate.now());
        if (!dishRepository.getByNameAndRestaurantIdAndDate(dish.getName(), dish.getRestaurant().getId(), dish.getDate())
                .isEmpty()) {
            throw new DuplicateDataException("Dish already exists");
        }
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurantWithDishToday"}, allEntries = true)
    public Dish update(Dish dish, int restaurantId) throws NotFoundException {
        Assert.notNull(dish, "dish must not be null");
        get(dish.getId());
        Restaurant restaurant = restaurantService.getById(restaurantId);
        dish.setRestaurant(restaurant);
        dish.setDate(LocalDate.now());
        if (!dishRepository.getByNameAndRestaurantIdAndDate(dish.getName(), dish.getRestaurant().getId(), dish.getDate())
                .isEmpty()) {
            throw new DuplicateDataException("Dish already exists");
        }
        checkNotFoundWithId(dishRepository.save(dish), dish.getId());
        return dish;
    }

    @CacheEvict(value = {"restaurants", "restaurantWithDishToday"}, allEntries = true)
    public void delete(int id) {
        dishRepository.deleteById(id);
    }
}
