package ru.test.bonus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.test.bonus.dao.UserDao;
import ru.test.bonus.dto.BalanceRqDto;
import ru.test.bonus.dto.BalanceRsDto;
import ru.test.bonus.dto.User;
import ru.test.bonus.security.MyUserDetails;
import ru.test.bonus.security.MyUserDetailsService;
import ru.test.bonus.security.SecurityConfig;
import ru.test.bonus.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BonusController.class)
@Import(SecurityConfig.class)
public class BonusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDao userDao;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    MyUserDetails principal = new MyUserDetails(new User(1, "test", "secret"));

    @Test
    @WithMockUser
    void add() {

    }

    @Test
    @WithMockUser
    void cancel() {
    }

    @Test
    @WithMockUser
    void returnPost() {
    }

    @Test
    @WithMockUser
    void balance() throws Exception {
        when(userService.getBalanceByCardNumber(new BalanceRqDto("a1"), 1))
                .thenReturn(new BalanceRsDto(100));
        mockMvc.perform(get("/balance")
                        .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "cardNumber": "a1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    @WithMockUser
    void history() {
    }
}