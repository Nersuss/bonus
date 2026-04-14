package ru.test.bonus.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.bonus.dto.RegisterDto;
import ru.test.bonus.service.UserService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegisterController {

    UserService userService;

    @PostMapping("/register")
    void registration(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
    }

}
