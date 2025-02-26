package ru.kata.spring.boot_security.demo.demo.model;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;


//Этот класс Role представляет собой сущность роли
// пользователя в системе и используется для
// управления правами доступа в Spring Security.

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    public Role(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Например, "ROLE_USER" или "ROLE_ADMIN"

    public Role() {

    }


    @Override
    public String getAuthority() {
        return name;
    }
    //Метод getAuthority() из интерфейса GrantedAuthority.
    //Возвращает имя роли (name), которое Spring Security использует для проверки прав доступа.
    //Как это работает в Spring Security?
    //Spring Security ожидает, что у каждого пользователя
    // будет список "GrantedAuthority" объектов (например, роли).
    //Класс Role реализует GrantedAuthority, поэтому
    // его можно использовать в качестве объекта,
    // представляющего роль.
    //Когда Spring проверяет, имеет ли пользователь доступ,
    // он сравнивает "ROLE_XYZ" из getAuthority() с тем,
    // что требуется в настройках безопасности (.hasRole("XYZ")).
    //Пример:
    //Если у пользователя есть роль "ROLE_ADMIN",
    // а защищенный ресурс требует .hasRole("ADMIN"),
    // Spring Security даст доступ.
}
