package ru.test.bonus.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.test.bonus.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDao {

    NamedParameterJdbcTemplate jdbcTemplate;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Boolean existByEmail(String email) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM public.users u WHERE u.email = :email);
                """;
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("email", email),
                Boolean.class
        );

    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, email, password FROM public.users u WHERE u.email = :email;
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("email", email),
                new DataClassRowMapper<>(User.class)
        ));
    }

    public int registerUser(RegisterDto registerDto) {
        String sql = """
                INSERT INTO public.users (email, password, balance, card_number) VALUES (:email, :password, :balance, :cardNumber);
                """;
        return jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("email", registerDto.login())
                        .addValue("password", bCryptPasswordEncoder().encode(registerDto.password()))
                        .addValue("balance", 100)
                        .addValue("cardNumber", UUID.randomUUID())
        );
    }

    public void addByCardNumber(AddDto addDto) {
        String sql = """
                UPDATE public.users SET balance = balance + :sum WHERE users.card_number = :cardNumber;
                """;
        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("sum", addDto.sum())
                        .addValue("cardNumber", addDto.cardNumber())
        );
    }

    public void insertOperation(int idUser, int sum) {
        String sql = """
                INSERT INTO public.operations (id_user, sum, date) VALUES (:idUser, :sum, :date);
                """;
        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("idUser", idUser)
                        .addValue("sum", sum)
                        .addValue("date", LocalDateTime.now())
        );
    }

    public void cancelByCardNumber(CancelDto cancelDto) {
        String sql = """
                UPDATE public.users SET balance = balance - :sum WHERE users.card_number = :cardNumber AND users.balance >= :sum;
                """;
        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("sum", cancelDto.sum())
                        .addValue("cardNumber", cancelDto.cardNumber())
        );
    }

    public BalanceRsDto getBalanceByCardNumber(BalanceRqDto balanceRqDto) {
        String sql = """
                SELECT u.balance FROM public.users u WHERE u.card_number = :cardNumber;
                """;
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("cardNumber", balanceRqDto.cardNumber()),
                new DataClassRowMapper<>(BalanceRsDto.class)
        );
    }

    public Integer returnByOperationId(ReturnRqDto returnRqDto) {
        String sql = """
                UPDATE public.users u SET balance = balance - o.sum FROM public.operations o WHERE u.id = o.id_user AND o.id = :idOperation RETURNING o.sum;
                """;
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("idOperation", returnRqDto.idOperation()),
                (rs, rowNum) -> rs.getInt("sum")
        );
    }

    public List<HistoryRsDto> getHistoryByCardNumber(HistoryRqDto historyRqDto) {
        String sql = """
                SELECT o.sum, o.date FROM public.operations o WHERE o.id_user = :cardNumber;
                """;
        return jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("cardNumber", historyRqDto.idUser()),
                new DataClassRowMapper<>(HistoryRsDto.class)
        );
    }

    public void initDb() {
        String sql = """
                CREATE TABLE IF NOT EXISTS public.users (
                    id SERIAL PRIMARY KEY,
                    email VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    balance INTEGER NOT NULL,
                    card_number VARCHAR(255) NOT NULL,
                    CONSTRAINT card_number UNIQUE(card_number),
                    CONSTRAINT email UNIQUE(email)
                );
                
                CREATE TABLE IF NOT EXISTS public.operations (
                    id SERIAL PRIMARY KEY,
                    id_user INTEGER,
                    sum INTEGER NOT NULL,
                    date TIMESTAMP NOT NULL,
                    FOREIGN KEY (id_user) REFERENCES public.users (id)
                );
                """;
        jdbcTemplate.getJdbcOperations().execute(sql);
    }

}
