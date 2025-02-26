package ru.kata.spring.boot_security.demo.demo.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Проверяем, есть ли роль ROLE_ADMIN
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                // Если есть роль ADMIN, перенаправляем на /admin
                response.sendRedirect("/admin");
                return;
            }
        }

        // Если роли ROLE_ADMIN нет, проверяем роль USER и перенаправляем на /user
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                response.sendRedirect("/user");
                return;
            }
        }

        // Если нет нужной роли, перенаправляем на главную
        response.sendRedirect("/");
    }
}
