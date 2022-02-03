/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.User;
import java.util.List;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
public interface UserDao {
    // basic CRUD operations
    User getUserById(int userId);
    List<User> getAllUsers();
    User addUser(User user);
    void updateUser(User user);
    void deleteUserById(int userId);
}
