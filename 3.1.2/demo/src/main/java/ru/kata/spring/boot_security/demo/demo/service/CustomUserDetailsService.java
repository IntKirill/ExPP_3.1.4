package ru.kata.spring.boot_security.demo.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.demo.repositories.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {


    //UserDetailsService — это стандартный интерфейс Spring Security,
    // который используется для загрузки
    // информации о пользователе (например, для аутентификации).


    private final UserRepository userRepository;
//Поле userRepository используется для взаимодействия с базой данных и поиска пользователей.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
//Переопределение метода loadUserByUsername из UserDetailsService.
//Этот метод вызывается Spring Security при попытке входа пользователя.
//Получает имя пользователя (username) и пытается загрузить его из базы данных.
//Если пользователь найден, метод возвращает объект UserDetails.
//Если пользователь не найден, выбрасывается исключение UsernameNotFoundException.

//userRepository.findByUsername(username) — пытается найти пользователя в базе данных.
//.orElseThrow(...) — если findByUsername возвращает Optional.empty(),
// то выбрасывается исключение UsernameNotFoundException.
//Если пользователь найден, он возвращается в виде UserDetails.