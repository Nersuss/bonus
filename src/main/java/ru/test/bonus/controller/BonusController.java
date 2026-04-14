package ru.test.bonus.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.bonus.dto.*;
import ru.test.bonus.security.MyUserDetails;
import ru.test.bonus.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BonusController {
    UserService userService;

    @PatchMapping("/add")
    void add(@RequestBody AddDto addDto, @AuthenticationPrincipal MyUserDetails principal) {
        userService.add(addDto, principal.getId());
    }

    @PatchMapping("/cancel")
    void cancel(@RequestBody CancelDto cancelDto, @AuthenticationPrincipal MyUserDetails principal) {
        userService.cancel(cancelDto, principal.getId());
    }

    @PatchMapping("/return")
    void returnPost(@RequestBody ReturnRqDto returnRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        userService.returnByOperationId(returnRqDto, principal.getId());
    }

    @GetMapping("/balance")
    BalanceRsDto balance(@RequestBody BalanceRqDto balanceRqDto) {
        return userService.getBalanceByCardNumber(balanceRqDto);
    }

    @GetMapping("/history")
    List<HistoryRsDto> history(@RequestBody HistoryRqDto historyRqDto) {
        return userService.getHistoryByCardNumber(historyRqDto);
    }

}
