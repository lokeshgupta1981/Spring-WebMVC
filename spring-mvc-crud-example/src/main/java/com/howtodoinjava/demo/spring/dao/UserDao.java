package com.howtodoinjava.demo.spring.dao;

import java.util.List;

import com.howtodoinjava.demo.spring.model.User;

public interface UserDao {
   void save(User user);
   List<User> list();
}
