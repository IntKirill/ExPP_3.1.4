package ru.kata.spring.boot_security.demo.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.demo.model.Role;
import ru.kata.spring.boot_security.demo.demo.model.User;
import ru.kata.spring.boot_security.demo.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.demo.repositories.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
public class AuthController {



    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository
            , PasswordEncoder passwordEncoder
            , RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }



    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users); // ✅ Добавляем пользователей в модель
        return "admin";
    }





    @GetMapping("/admin/new")
    public String CreateUserForm(@ModelAttribute("user") User user) {
        return "new";
    }
    @PostMapping("/admin/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        userRepository.save(user);
        return "redirect:/admin"; // Перенаправляем админа обратно в админку

    }
    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam("id") long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return "redirect:/admin";
    }



    @GetMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')") // ✅ Только админы могут редактировать пользователей
    public String getEditUserForm(@RequestParam("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")); // ✅ Если пользователя нет, ошибка

        model.addAttribute("user", user); // ✅ Передаем объект User в модель
        return "update"; // ✅ Показываем страницу "update.html"
    }

    @PostMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')") // ✅ Только админы могут обновлять пользователей
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update"; // ✅ Если есть ошибки, возвращаем форму редактирования
        }

        userRepository.save(user); // ✅ В Spring Data JPA обновление идет через `save()`
        return "redirect:/admin"; // ✅ После обновления — редирект в админку
    }




    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        // Получаем имя текущего пользователя
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        // Здесь вы можете получить данные о пользователе из базы данных
        // например, userService.findUserByUsername(username)
        model.addAttribute("user", user);

        // Возвращаем представление для страницы пользователя
        return "user";
    }





}

