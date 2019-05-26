package com.example.demo.utils;

import com.example.demo.dao.FriendDao;
import com.example.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    private static UserDao userDao;

    private static FriendDao friendDao;

    @Autowired
    public UserUtil(UserDao userDao,FriendDao friendDao) {
        UserUtil.userDao = userDao;
        UserUtil.friendDao=friendDao;
    }

    public static int getId(String username){
        return Integer.parseInt(userDao.getId(username));
    }

    public static String getStatus(int userId, int receiveId){
        return friendDao.friendship(userId,receiveId);
    }




}
