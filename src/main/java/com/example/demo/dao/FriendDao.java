package com.example.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FriendDao {
    @Insert("insert into friend set user_id=#{userId},receive_id=#{receiveId},status='3'")
    boolean addFriend(@Param("userId") int userId,@Param("receiveId") int receiveId);

    @Update("update friend status=#{status} where user_id=#{userId} and receive_id=#{receiveId}")
    int respondUser(@Param("status") int status,@Param("userId") int userID,@Param("receiveId") int receiveId);

    @Select("select status from friend where user_id=#{userId} and receive_id=#{receiveId}")
    String friendship(@Param("userId") int userID,@Param("receiveId") int receiveId);
}
