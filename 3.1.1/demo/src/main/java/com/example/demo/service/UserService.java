package com.example.demo.service;


import com.example.demo.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findById(Long id);

    void removeUserById(long id);

    void updateUser(User user);

    List<User> findAll();
}
