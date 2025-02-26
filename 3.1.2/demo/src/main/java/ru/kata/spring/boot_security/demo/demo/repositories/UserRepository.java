package ru.kata.spring.boot_security.demo.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.demo.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
//UserRepository — это интерфейс, который отвечает за взаимодействие с базой данных.
//extends JpaRepository<User, Long> — означает, что этот репозиторий наследуется от
// JpaRepository, что дает ему уже готовые методы CRUD
// (Create, Read, Update, Delete) для сущности User.