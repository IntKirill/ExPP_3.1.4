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

import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


@Autowired
    public AuthController(UserRepository userRepository
            , PasswordEncoder passwordEncoder
            , RoleRepository roleRepository, RoleService roleService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

@GetMapping("/user")
public String userPage(Model model, Principal principal) {
    String username = principal.getName();
    User user = userRepository.findByUsername(username).orElse(null);
    model.addAttribute("user", user);
    return "user";
}

@GetMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public String getUsers(Model model, @ModelAttribute("error") String error
        , @ModelAttribute("message") String message) {
    List<User> users = userRepository.findAll();
    model.addAttribute("users", users);
    if (error != null && !error.isEmpty()) {
        model.addAttribute("error", error);
    }
    if (message != null && !message.isEmpty()) {
        model.addAttribute("message", message);
    }
    return "admin";
}


    @GetMapping("/admin/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/admin/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Form has errors");
            return "new";
        }
        Role userRole = roleService.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Role 'ROLE_USER' not found"));
        user.getRoles().add(userRole);
        System.out.println("Password encoding...");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        System.out.println("Saving user...");
        userRepository.save(user);
        System.out.println("Redirecting to /admin...");
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        System.out.println("Deleting user with id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " not found"));

        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error",
                    "Нельзя удалить администратора!");
            return "redirect:/admin";
        }
        userRepository.delete(user);
        System.out.println("User deleted");
        redirectAttributes.addFlashAttribute("message",
                "Пользователь успешно удалён!");
        return "redirect:/admin";
    }

    @GetMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String getEditUserForm(@RequestParam("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().size();
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@RequestParam("id") Long id,
                             @ModelAttribute("user") User user,
                             RedirectAttributes redirectAttributes) {
        System.out.println("Update user with id: " + id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " not found"));

        if (existingUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Нельзя изменить администратора!");
            return "redirect:/admin";  // Перенаправляем на страницу списка пользователей
        }
        existingUser.setUsername(user.getUsername());
        existingUser.setCountry(user.getCountry());
        existingUser.setCar(user.getCar());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(existingUser);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно обновлен!");

        return "redirect:/admin";  // Перенаправляем на страницу списка пользователей
    }


}




