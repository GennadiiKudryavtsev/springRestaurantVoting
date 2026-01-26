package com.kudryavtsevgennady.springrestaurantvoting.spring.web.dish;

import com.kudryavtsevgennady.springrestaurantvoting.spring.model.Dish;
import com.kudryavtsevgennady.springrestaurantvoting.spring.service.DishService;
import com.kudryavtsevgennady.springrestaurantvoting.spring.web.AbstractControllerTest;
import com.kudryavtsevgennady.springrestaurantvoting.spring.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static com.kudryavtsevgennady.springrestaurantvoting.spring.DishTestData.*;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.RestaurantTestData.RESTAURANT_ID_1;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.TestUtil.readFromJson;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.TestUtil.userHttpBasic;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.UserTestData.ADMIN;
import static com.kudryavtsevgennady.springrestaurantvoting.spring.UserTestData.USER_1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminDishController.REST_URL;

    @Autowired
    private DishService dishService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/dishes/" + DISH_ID_1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        DISH_MATCHER.assertMatch(dishService.get(DISH_ID_1), DISH_1);
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/restaurants/" + RESTAURANT_ID_1 + "/dishes")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        DISH_MATCHER.assertMatch(dishService.getAll(RESTAURANT_ID_1), Arrays.asList(DISH_1, DISH_2, DISH_3));
    }

    @Test
    void save() throws Exception {
        Dish created = getCreated();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/restaurants/" + RESTAURANT_ID_1 + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));
        Dish returned = readFromJson(action, Dish.class);
        created.setId(returned.getId());
        DISH_MATCHER.assertMatch(dishService.getAll(RESTAURANT_ID_1), Arrays.asList(DISH_1, DISH_2, DISH_3, created));
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/restaurants/" + RESTAURANT_ID_1 + "/dishes/" + DISH_ID_1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.get(DISH_ID_1), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/dishes/" + DISH_ID_1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.getAll(RESTAURANT_ID_1), Arrays.asList(DISH_2, DISH_3));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/dishes/" + DISH_ID_3))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/dishes/" + DISH_ID_2)
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/dishes/1")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}