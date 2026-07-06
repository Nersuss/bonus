package ru.test.bonus.dto.jwt;

public record JwtRs(
        String accessToken,
        String refreshToken
) {
}
