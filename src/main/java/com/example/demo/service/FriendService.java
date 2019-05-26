package com.example.demo.service;

public interface FriendService {
    boolean addFriend(int userId,int receiveId);
    String respondUser(int status,int userId,int receiveId);
}
