package com.example.demo.service;

import com.example.demo.pojo.UserBean;

public interface UserService {
    boolean addUser(UserBean userBean);
    boolean loginUser(String username,String password);
}
