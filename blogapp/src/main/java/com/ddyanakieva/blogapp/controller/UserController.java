/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.controller;

import com.ddyanakieva.blogapp.dao.BlogDao;
import com.ddyanakieva.blogapp.dao.ImageDao;
import com.ddyanakieva.blogapp.dao.TagDao;
import com.ddyanakieva.blogapp.dao.UserDao;
import com.ddyanakieva.blogapp.entities.Blog;
import com.ddyanakieva.blogapp.entities.Image;
import com.ddyanakieva.blogapp.entities.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @date 27-Aug-2021
 * @author ddyanakieva purpose:
 */
@Controller
public class UserController {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    // a class variable to hold the set of ConstraintViolations from our Validator:
    Set<ConstraintViolation<User>> violations = new HashSet<>();

    @GetMapping("users")
    public String displayUsers(Model model) {
        List<User> users = userDao.getAllUsers();
        List<Image> images = imageDao.getAllImages();
        model.addAttribute("users", users);
        model.addAttribute("errors", violations);
        model.addAttribute("images", images);
        return "users";
    }

    @PostMapping("addUser")
    public String addUser(User user, Integer imageId) {
        user.setProfilePic(imageDao.getImageById(imageId));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(user);

        if (violations.isEmpty()) {
            userDao.addUser(user);
        }
        return "redirect:/users";
    }

    @GetMapping("editUser")
    public String editUser(Integer userId, Model model) {
        User user = userDao.getUserById(userId);
        List<Image> images = imageDao.getAllImages();
        model.addAttribute("user", user);
        model.addAttribute("images", images);
        return "editUser";
    }

//    @PostMapping("editUser")
//    public String performEditUser(User user, Integer imageId) {
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        user.setProfilePic(imageDao.getImageById(imageId));
//
//        violations = validator.validate(user);
//        if (violations.isEmpty()) {
//            userDao.updateUser(user);
//            return "redirect:/users";
//        }        
//        String redirect = "redirect:/editUser?userId="+user.getUserId();
//        return redirect;
//    }
    @PostMapping("editUser")
    public String performEditUser(@Valid User user, BindingResult result, Integer imageId, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("images", imageDao.getAllImages());
            model.addAttribute("user", user);
            return "editUser";
        }
        user.setProfilePic(imageDao.getImageById(imageId));

        userDao.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("deleteUser")
    public String deleteUser(Integer userId) {
        userDao.deleteUserById(userId);
        return "redirect:/users";
    }

    @GetMapping("userDetails")
    public String userDetails(Integer userId, Model model) {
        User user = userDao.getUserById(userId);
        List<Blog> blogs = blogDao.getBlogsForUser(user);
        model.addAttribute("user", user);
        model.addAttribute("blogs", blogs);
        return "userDetails";
    }
}
