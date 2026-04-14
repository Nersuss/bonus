package ru.test.bonus;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.test.bonus.dao.UserDao;

@SpringBootApplication
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BonusApplication implements CommandLineRunner {

    UserDao userDao;

    public static void main(String[] args) {
        SpringApplication.run(BonusApplication.class, args);
    }

    @Override
    public void run(@Nullable String... args) {
        userDao.initDb();
    }
}
