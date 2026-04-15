package ru.test.bonus.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.bonus.dao.UserDao;
import ru.test.bonus.dto.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserDao userDao;

    public void register(RegisterRqDto registerRqDto) {
        if (userDao.existByEmail(registerRqDto.login())) {
            System.out.println("User already exist");
            return;
        }
        userDao.registerUser(registerRqDto);
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
