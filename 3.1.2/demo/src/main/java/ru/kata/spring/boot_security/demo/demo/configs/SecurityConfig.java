package ru.kata.spring.boot_security.demo.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
public class SecurityConfig  {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //PasswordEncoder — интерфейс,
    // предоставляющий методы для шифрования и проверки паролей.
    //BCryptPasswordEncoder — один из стандартных алгоритмов для хеширования паролей,
    // который используется для шифрования паролей в базе данных.
    //Возвращаемое значение — экземпляр BCryptPasswordEncoder,
    // который будет использоваться для хеширования паролей.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    //AuthenticationManager — интерфейс, который отвечает за обработку аутентификации
    //пользователя. Он проверяет учетные данные и решает, разрешено ли пользователю войти.
    //AuthenticationConfiguration — предоставляет доступ к настройкам аутентификации в приложении.
    //Метод возвращает объект AuthenticationManager,
    // используя authenticationConfiguration.getAuthenticationManager().
    //@Bean говорит Spring, что этот метод создает бин для менеджера аутентификации,
    // который будет использоваться для проверки учетных данных при входе пользователя.



        private final LoginSuccessHandler loginSuccessHandler;

        // Внедряем LoginSuccessHandler через конструктор
    public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
            this.loginSuccessHandler = loginSuccessHandler;
        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Доступ только для админов
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // Доступ для USER и ADMIN
                        .anyRequest().authenticated()   // Доступ только для пользователей
                )
                .formLogin(form -> form
                        .permitAll()  // Разрешаем всем доступ к странице логина
                        .successHandler(loginSuccessHandler)  // Указываем кастомный обработчик успешного входа

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL для логаута
                        .logoutSuccessUrl("/login?logout")  // Перенаправление после выхода
                        .permitAll()  // Разрешаем всем пользователям выходить
                );
        return http.build();
    }
}
