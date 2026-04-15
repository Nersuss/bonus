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
import ru.test.bonus.dto.*;
import ru.test.bonus.security.MyUserDetails;
import ru.test.bonus.security.MyUserDetailsService;
import ru.test.bonus.security.SecurityConfig;
import ru.test.bonus.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    void add() throws Exception {
        when(userService.add(new AddRqDto("a1", 15), 1))
                .thenReturn(true);
        mockMvc.perform(patch("/add")
                        .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sum": "15",
                                    "cardNumber": "a1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Начисление успешно выполнено"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void cancel() throws Exception {
        when(userService.cancel(new CancelRqDto("a1", 15), 1))
                .thenReturn(true);
        mockMvc.perform(patch("/cancel")
                        .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sum": "15",
                                    "cardNumber": "a1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Списание успешно выполнено"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void returnPost() throws Exception {
        when(userService.returnByOperationId(new ReturnRqDto(15), 1))
                .thenReturn(true);
        mockMvc.perform(patch("/return")
                        .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idOperation": "15"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Возврат успешно выполнен"))
                .andExpect(jsonPath("$.data").isEmpty());
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
    void history() throws Exception {
        when(userService.getHistoryByCardNumber(new HistoryRqDto("a1"), 1))
                .thenReturn((List.of(new HistoryRsDto(15, LocalDateTime.now()))));
        mockMvc.perform(get("/history")
                        .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "cardNumber": "a1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sum").value(15));
    }
}
