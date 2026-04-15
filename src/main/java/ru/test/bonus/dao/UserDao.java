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

    public void registerUser(RegisterRqDto registerRqDto) {
        String sql = """
                INSERT INTO public.users (email, password, balance, card_number) VALUES (:email, :password, :balance, :cardNumber);
                """;
        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("email", registerRqDto.login())
                        .addValue("password", bCryptPasswordEncoder().encode(registerRqDto.password()))
                        .addValue("balance", 100)
                        .addValue("cardNumber", UUID.randomUUID())
        );
    }

    public int addByCardNumber(AddRqDto addRqDto, int idUser) {
        String sql = """
                UPDATE public.users SET balance = balance + :sum WHERE users.card_number = :cardNumber AND :sum > 0 AND users.id = :cardNumber;
                """;
        return jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("sum", addRqDto.sum())
                        .addValue("cardNumber", addRqDto.cardNumber())
                        .addValue("cardNumber", idUser)
        );
    }

    public int cancelByCardNumber(CancelRqDto cancelRqDto, int idUser) {
        String sql = """
                UPDATE public.users SET balance = balance - :sum WHERE users.card_number = :cardNumber AND :sum > 0 AND users.balance >= :sum AND users.id = :cardNumber;
                """;
        return jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("sum", cancelRqDto.sum())
                        .addValue("cardNumber", cancelRqDto.cardNumber())
                        .addValue("cardNumber", idUser)
        );
    }

    public void insertOperation(int idUser, int sum) {
        String sql = """
                INSERT INTO public.operations (id_user, sum, date) VALUES (:cardNumber, :sum, :date);
                """;
        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("cardNumber", idUser)
                        .addValue("sum", sum)
                        .addValue("date", LocalDateTime.now())
        );
    }

    public Integer returnByOperationId(ReturnRqDto returnRqDto, int idUser) {
        String sql = """
                UPDATE public.users u SET balance = balance - o.sum FROM public.operations o WHERE u.id = :cardNumber AND o.id = :idOperation RETURNING o.sum;
                """;
        List<Integer> res = jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("idOperation", returnRqDto.idOperation())
                        .addValue("cardNumber", idUser),
                (rs, rowNum) -> rs.getInt("sum")
        );
        return res.isEmpty() ? -1 : res.getFirst();
    }

    public BalanceRsDto getBalanceByCardNumber(BalanceRqDto balanceRqDto, int idUser) {
        String sql = """
                SELECT u.balance FROM public.users u WHERE u.card_number = :cardNumber AND u.id = :idUser;
                """;
        List<BalanceRsDto> res = jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("cardNumber", balanceRqDto.cardNumber())
                        .addValue("idUser", idUser),
                new DataClassRowMapper<>(BalanceRsDto.class)
        );
        return res.isEmpty() ? new BalanceRsDto(null) : res.getFirst();
    }

    public List<HistoryRsDto> getHistoryByCardNumber(HistoryRqDto historyRqDto, int idUser) {
        String sql = """
                SELECT o.sum, o.date FROM public.operations o JOIN users u ON u.id = :idUser WHERE u.card_number = :cardNumber;
                """;
        return jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("cardNumber", historyRqDto.cardNumber())
                        .addValue("idUser", idUser),
                new DataClassRowMapper<>(HistoryRsDto.class)
        );
    }

}
