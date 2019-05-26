package com.example.demo.controller;

import com.example.demo.dao.FriendDao;
import com.example.demo.pojo.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserBean userBean;
    @Autowired
    FriendDao friendDao;
    public String addUser(String username,String password,String emali,String telephone){
        userBean.setUsername(username);
        userBean.setPassword(password);
        userBean.setEmail(emali);
        userBean.setTelephone(telephone);
        Boolean flag=userService.addUser(userBean);
        if (flag){
            return "跳转到登录页面！";
        }else {
            return "注册失败，回到注册页面重新注册！";
        }
    }

    public String loginUser(String username,String password){
        if (userService.loginUser(username,password)){
            return "ws://localhost:8080/websocket/"+username;
        }else {
            return "登录失败！";
        }
    }

    @GetMapping("test")
    public String test(){
        return friendDao.friendship(9,10);
    }
}
