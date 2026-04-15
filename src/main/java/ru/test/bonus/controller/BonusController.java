package ru.test.bonus.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.bonus.dto.*;
import ru.test.bonus.security.MyUserDetails;
import ru.test.bonus.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BonusController {
    UserService userService;

    @PatchMapping("/add")
    ResponseEntity<Map<String, Object>> add(@RequestBody AddRqDto addRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        Map<String, Object> response = new HashMap<>();
        if (userService.add(addRqDto, principal.getId())) {
            response.put("success", true);
            response.put("data", null);
            response.put("message", "Начисление успешно выполнено");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("data", null);
        response.put("message", "Не удалось выполнить операцию");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PatchMapping("/cancel")
    ResponseEntity<Map<String, Object>> cancel(@RequestBody CancelRqDto cancelRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        Map<String, Object> response = new HashMap<>();
        if (userService.cancel(cancelRqDto, principal.getId())) {
            response.put("success", true);
            response.put("data", null);
            response.put("message", "Списание успешно выполнено");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("data", null);
        response.put("message", "Не удалось выполнить операцию");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PatchMapping("/return")
    ResponseEntity<Map<String, Object>> returnPost(@RequestBody ReturnRqDto returnRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        Map<String, Object> response = new HashMap<>();
        if (userService.returnByOperationId(returnRqDto, principal.getId())) {
            response.put("success", true);
            response.put("data", null);
            response.put("message", "Возврат успешно выполнен");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("data", null);
        response.put("message", "Не удалось выполнить операцию");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/balance")
    BalanceRsDto balance(@RequestBody BalanceRqDto balanceRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        return userService.getBalanceByCardNumber(balanceRqDto, principal.getId());
    }

    @GetMapping("/history")
    List<HistoryRsDto> history(@RequestBody HistoryRqDto historyRqDto, @AuthenticationPrincipal MyUserDetails principal) {
        return userService.getHistoryByCardNumber(historyRqDto, principal.getId());
    }

}
