/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.dao.ImageDaoDB.ImageMapper;
import com.ddyanakieva.blogapp.entities.Image;
import com.ddyanakieva.blogapp.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva purpose:
 */
@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public User getUserById(int userId) {
        final String SELECT_USER = "SELECT * FROM user WHERE userId = ?";
        try {
            User user = jdbc.queryForObject(SELECT_USER, new UserMapper(), userId);
            user.setProfilePic(getImageForUser(userId));
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    // helper method
    private Image getImageForUser(int userId){
        return jdbc.queryForObject("SELECT * FROM image i JOIN user u "
                + "ON u.profilePic = i.imageId "
                + "WHERE u.userId = ?", new ImageMapper(), userId);
    }
    
    // helper method
    private void associateUserAndImage(List<User> users){
        for (User user : users){
            user.setProfilePic(this.getImageForUser(user.getUserId()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM user";
        List<User> users = jdbc.query(SELECT_ALL_USERS, new UserMapper());
        this.associateUserAndImage(users);
        return users;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        // add the user to the db & fetch the auto generated id
        final String INSERT_USER = "INSERT INTO user(firstName, lastName, isAdmin, profilePic) VALUES (?, ?, ?, ?)";
        jdbc.update(INSERT_USER, 
                user.getFirstName(), 
                user.getLastName(),
                user.isIsAdmin(),
                user.getProfilePic().getImageId());
        int userId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setUserId(userId);
        return user;
    }


    @Override
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE user SET firstName = ?, lastName = ?, isAdmin = ?, profilePic = ? "
                + "WHERE userId = ?";
        jdbc.update(UPDATE_USER, 
                user.getFirstName(), 
                user.getLastName(),
                user.isIsAdmin(),
                user.getProfilePic().getImageId(),
                user.getUserId());
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        // first delete from foreign tables referencing the primary key -- userId
        final String DELETE_USERAUTH = "DELETE FROM userAuth "
                + "WHERE userId = ?";
        jdbc.update(DELETE_USERAUTH, userId);
        final String DELETE_BLOG_USER = "DELETE FROM blogUser "
                + "WHERE userId = ?";
        jdbc.update(DELETE_BLOG_USER, userId);
        
        // delete from the main table
        final String DELETE_USER = "DELETE FROM user WHERE userId = ?";
        jdbc.update(DELETE_USER, userId);
    }
    
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setIsAdmin(rs.getBoolean("isAdmin"));
            return user;
        }        
    }
}
