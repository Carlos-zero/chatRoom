package com.example.demo.service;

import com.example.demo.dao.FriendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    FriendDao friendDao;
    @Override
    public boolean addFriend(int userId, int receiveId) {
        boolean flag=friendDao.addFriend(userId,receiveId);
        return flag;
    }

    @Override
    public String respondUser(int status, int userId, int receiveId) {
        int statusNow=friendDao.respondUser(status, userId, receiveId);
        if (statusNow==1){
            return "添加好友成功！";
        }else if (statusNow==2){
            return "对方拒绝添加您！";
        }else if (statusNow==3){
            return "对方为响应您的添加请求！";
        }
        return "添加好友失败！";
    }
}
