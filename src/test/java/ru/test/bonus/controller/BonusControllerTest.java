package ru.test.bonus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.test.bonus.dao.UserDao;
import ru.test.bonus.dto.BalanceRqDto;
import ru.test.bonus.dto.BalanceRsDto;
import ru.test.bonus.security.MyUserDetailsService;
import ru.test.bonus.security.SecurityConfig;
import ru.test.bonus.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BonusController.class)
@Import(SecurityConfig.class)
public class BonusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDao userRepo;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @Test
    @WithMockUser
    void add() {

    }

    @Test
    void cancel() {
    }

    @Test
    void returnPost() {
    }

    @Test
    @WithMockUser
    void balance() throws Exception {
        when(userRepo.getBalanceByCardNumber(new BalanceRqDto("a1")))
                .thenReturn(new BalanceRsDto(100));

        mockMvc.perform(get("/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"a1\"}"))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void history() {
    }
}