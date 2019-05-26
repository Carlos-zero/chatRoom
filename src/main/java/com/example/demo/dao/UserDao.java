package com.example.demo.dao;

import com.example.demo.pojo.UserBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserDao {
    @Insert("insert into user set user_name=#{username},password=#{password},email=#{email},telephone=#{telephone}")
    boolean addUser(UserBean userBean);

    @Select("select * from user where user_name=#{username} and password=#{password}")
    UserBean getUser(@Param("username") String username,@Param("password") String password);

    @Select("select id from user where user_name=#{username}")
    String getId(String username);

}
