package ru.kata.spring.boot_security.demo.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.demo.model.Role;
import ru.kata.spring.boot_security.demo.demo.repositories.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

@Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Метод для поиска роли по имени
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    // Метод для сохранения роли
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
