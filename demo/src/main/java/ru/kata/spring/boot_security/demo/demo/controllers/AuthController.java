
package ru.kata.spring.boot_security.demo.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.demo.model.Role;
import ru.kata.spring.boot_security.demo.demo.model.User;
import ru.kata.spring.boot_security.demo.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.demo.service.UserService;
import ru.kata.spring.boot_security.demo.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AuthController {


    private final UserService userService;
    private final RoleService roleService;



    @Autowired
    public AuthController(
            UserService userService
            , RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String adminPanel() {
        return "admin";
    }

    @GetMapping("/user")
    public String usernPage() {
        return "user";
    }



}





