package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
@GetMapping("/")
public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
        //главная странница
}


@GetMapping("/new")
    public String CreateUserForm(@ModelAttribute("user") User user) {
        return "new";
        //создание пользователя
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.removeUserById(id);
        return "redirect:/";
        //удаление нового пользователя по ид
    }

   @GetMapping("/update")
    public String getEditUserForm(Model model, @RequestParam("id") long id) {
        model.addAttribute("user", userService.findById(id));
        return "update";
       //Получение пользователя которого хотим изменить
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.updateUser(user);
        return "redirect:/";
        //изменяем полученного пользователя

    }
}






