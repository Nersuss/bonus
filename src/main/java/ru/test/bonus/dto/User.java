package ru.test.bonus.dto;

public record User (
        Integer id,
        String email,
        String password
) {
}