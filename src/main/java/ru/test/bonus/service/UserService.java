package ru.test.bonus.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.bonus.dao.UserDao;
import ru.test.bonus.dto.*;
import ru.test.bonus.dto.jwt.JwtProvider;
import ru.test.bonus.dto.jwt.JwtRq;
import ru.test.bonus.dto.jwt.JwtRs;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserDao userDao;
    JwtProvider jwtProvider;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public JwtRs register(RegisterRqDto registerRqDto) {
        if (userDao.existByEmail(registerRqDto.login())) {
            return null;
        }
        userDao.registerUser(registerRqDto);
        String accessToken = jwtProvider.generateAccessToken(registerRqDto.login());
        String refreshToken = jwtProvider.generateRefreshToken(registerRqDto.password());
        userDao.updateRefreshToken(registerRqDto.login(), refreshToken);
        return new JwtRs(accessToken, refreshToken);
    }

    public JwtRs login(JwtRq authRequest) {
        Optional<User> user = userDao.findByEmail(authRequest.login());
        if (user.isEmpty() || !bCryptPasswordEncoder.matches(authRequest.password(), user.get().password())) {
            throw new RuntimeException();
        }

        String accessToken = jwtProvider.generateAccessToken(authRequest.login());
        String refreshToken = jwtProvider.generateRefreshToken(authRequest.login());
        userDao.updateRefreshToken(authRequest.login(), refreshToken);

        return new JwtRs(accessToken, refreshToken);
    }

    @Transactional
    public boolean add(AddRqDto addRqDto, int idUser) {
        int res = userDao.addByCardNumber(addRqDto, idUser);
        if (res == 1) {
            userDao.insertOperation(idUser, addRqDto.sum());
        }
        return res == 1;
    }

    @Transactional
    public boolean cancel(CancelRqDto cancelRqDto, int idUser) {
        int res = userDao.cancelByCardNumber(cancelRqDto, idUser);
        if (res == 1) {
            userDao.insertOperation(idUser, -cancelRqDto.sum());
        }
        return res == 1;
    }

    @Transactional
    public boolean returnByOperationId(ReturnRqDto returnRqDto, int idUser) {
        Integer sum = userDao.returnByOperationId(returnRqDto, idUser);
        if (sum != -1) {
            userDao.insertOperation(idUser, -sum);
        }
        return sum != -1;
    }

    public BalanceRsDto getBalanceByCardNumber(BalanceRqDto balanceRqDto, int idUser) {
        return userDao.getBalanceByCardNumber(balanceRqDto, idUser);
    }

    public List<HistoryRsDto> getHistoryByCardNumber(HistoryRqDto historyRqDto, int idUser) {
        return userDao.getHistoryByCardNumber(historyRqDto, idUser);
    }

}
