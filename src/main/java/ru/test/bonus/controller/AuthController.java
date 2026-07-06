package ru.test.bonus.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.test.bonus.dto.RegisterRqDto;
import ru.test.bonus.dto.jwt.JwtRq;
import ru.test.bonus.dto.jwt.JwtRs;
import ru.test.bonus.service.UserService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    UserService userService;

    @PostMapping("/register")
    JwtRs registration(@RequestBody RegisterRqDto registerRqDto) {
        return userService.register(registerRqDto);
    }

    @PostMapping("login")
    public ResponseEntity<JwtRs> login(@RequestBody JwtRq authRequest) {
        final JwtRs token = userService.login(authRequest);
        return ResponseEntity.ok(token);
    }

}
