package com.kudryavtsevgennady.springrestaurantvoting.spring.web.dish;

import com.kudryavtsevgennady.springrestaurantvoting.spring.model.Dish;
import com.kudryavtsevgennady.springrestaurantvoting.spring.service.DishService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.kudryavtsevgennady.springrestaurantvoting.spring.util.ValidationUtil.assureIdConsistent;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "/rest/admin";
    private static final Logger log = LoggerFactory.getLogger(AdminDishController.class);

    private final DishService dishService;

    public AdminDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping(value = "/dishes/{id}")
    public Dish get(@PathVariable("id") int id) throws NotFoundException {
        log.info("get dish by Id={}", id);
        return dishService.get(id);
    }

    @GetMapping(value = "/restaurants/{restaurantId}/dishes")
    public List<Dish> getAllByRestaurantId(@PathVariable("restaurantId") int restaurantId) throws NotFoundException {
        log.info("get all dishes by restaurant id={}", restaurantId);
        return dishService.getAll(restaurantId);
    }

    @PostMapping(value = "/restaurants/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> save(@Valid @RequestBody Dish dish, @PathVariable("restaurantId") int restaurantId) throws NotFoundException {
        checkNew(dish);
        log.info("create dish in restaurant id={}", restaurantId);
        Dish created = dishService.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/" + "dishes/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/restaurants/{restaurantId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Dish update(@Valid @RequestBody Dish dish, @PathVariable("restaurantId") int restaurantId, @PathVariable("id") int id) throws NotFoundException {
        assureIdConsistent(dish, id);
        log.info("update dish with id={}", id);
        return dishService.update(dish, restaurantId);
    }

    @DeleteMapping(value = "/dishes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("delete dish with id={}", id);
        dishService.delete(id);
    }
}
