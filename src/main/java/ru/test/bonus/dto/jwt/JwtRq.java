package ru.test.bonus.dto.jwt;

public record JwtRq(
        String login,
        String password
) {
}
