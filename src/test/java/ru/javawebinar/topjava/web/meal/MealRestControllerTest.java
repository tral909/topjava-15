package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.*;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    private final static String BASE_URL = MealRestController.REST_URL + "/";

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(BASE_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(BASE_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk());
         assertMatch(mealService.getAll(SecurityUtil.authUserId()),
                 MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2016, Month.JUNE, 21, 17, 0), "Полдник", 800);
        ResultActions action = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isOk());
        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(SecurityUtil.authUserId()),
                expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }
}
