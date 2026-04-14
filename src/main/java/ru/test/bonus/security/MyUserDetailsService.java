package ru.test.bonus.security;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.test.bonus.dao.UserDao;
import ru.test.bonus.dto.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MyUserDetailsService implements UserDetailsService {

    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(@Nullable String username) throws UsernameNotFoundException {

        Optional<User> user = userDao.findByEmail(username);

        if (user.isPresent()) {
            return new MyUserDetails(user.get());
        }
        throw new UsernameNotFoundException("Ошибка аутентификации: " + username);
    }

}
