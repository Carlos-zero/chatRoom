package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.pojo.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public boolean addUser(UserBean userBean) {
        return userDao.addUser(userBean);
    }

    @Override
    public boolean loginUser(String username, String password) {
        UserBean userBean=userDao.getUser(username,password);
        if (userBean!=null){
            return true;
        }
        return false;
    }
}
