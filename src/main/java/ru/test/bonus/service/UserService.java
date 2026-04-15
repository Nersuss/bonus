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
    public void add(AddRqDto addRqDto, int idUser) {
        userDao.addByCardNumber(addRqDto);
        userDao.insertOperation(idUser, addRqDto.sum());
    }

    @Transactional
    public void cancel(CancelRqDto cancelRqDto, int idUser) {
        userDao.cancelByCardNumber(cancelRqDto);
        userDao.insertOperation(idUser, -cancelRqDto.sum());
    }

    public BalanceRsDto getBalanceByCardNumber(BalanceRqDto balanceRqDto) {
        return userDao.getBalanceByCardNumber(balanceRqDto);
    }

    @Transactional
    public void returnByOperationId(ReturnRqDto returnRqDto, int idUser) {
        Integer sum = userDao.returnByOperationId(returnRqDto);
        userDao.insertOperation(idUser, -sum);
    }

    public List<HistoryRsDto> getHistoryByCardNumber(HistoryRqDto historyRqDto) {
        return userDao.getHistoryByCardNumber(historyRqDto);
    }

}
