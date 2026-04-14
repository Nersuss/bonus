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

    public void register(RegisterDto registerDto) {
        if (userDao.existByEmail(registerDto.login())) {
            System.out.println("User already exist");
            return;
        }
        userDao.registerUser(registerDto);
    }

    @Transactional
    public void add(AddDto addDto, int idUser) {
        userDao.addByCardNumber(addDto);
        userDao.insertOperation(idUser, addDto.sum());
    }

    @Transactional
    public void cancel(CancelDto cancelDto, int idUser) {
        userDao.cancelByCardNumber(cancelDto);
        userDao.insertOperation(idUser, -cancelDto.sum());
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
