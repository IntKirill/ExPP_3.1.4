package ru.kata.spring.boot_security.demo.demo.configs;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.demo.model.Role;
import ru.kata.spring.boot_security.demo.demo.model.User;
import ru.kata.spring.boot_security.demo.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.demo.repositories.UserRepository;

import java.util.Set;


//Этот класс DataInitializer предназначен для
// инициализации базы данных начальными данными
// при запуске приложения. Он проверяет,
// существуют ли роли и администратор в
// базе, и если нет, создает их

@Configuration
public class DataInitializer {


    @Bean
    public ApplicationRunner initData(UserRepository userRepository
            , RoleRepository roleRepository
            , PasswordEncoder passwordEncoder) {
        return args -> initializeData(userRepository, roleRepository, passwordEncoder);
    }

    private void initializeData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        // Добавляем роли, если их нет
        Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() -> roleRepository
                        .save(new Role("ROLE_USER")));//Выполняется создание роли
        Role adminRole = roleRepository
                .findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository
                        .save(new Role("ROLE_ADMIN")));//Выполняется создание роли

        // Добавляем пользователя-админа, если его нет
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole, userRole)); // Админ получает обе роли
            userRepository.save(admin);
            //Создается новый объект User с логином "admin".
            //setPassword(passwordEncoder.encode("admin123"))
            //Пароль "admin123" хешируется с помощью BCryptPasswordEncoder,
            // чтобы он не хранился в открытом виде.
            //setRoles(Set.of(adminRole, userRole))
            //Администратор получает две роли: "ROLE_ADMIN" и "ROLE_USER".
            //userRepository.save(admin)
            //Админ сохраняется в базу данных.
        }
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user"); // Имя обычного пользователя
            user.setPassword(passwordEncoder.encode("user123")); // Пароль обычного пользователя
            user.setRoles(Set.of(userRole)); // Назначаем только роль ROLE_USER
            userRepository.save(user);
        }
    }
}