package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserServiceImp implements UserService {


@Autowired
public UserDao userDao;

    @Transactional
    @Override
    public void save(User user) {
        userDao.save(user);

    }
@Transactional
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }


    @Transactional
    @Override
    public void removeUserById(long id) {
        userDao.removeUserById(id);
    }



@Transactional
    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);

    }
@Transactional
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
