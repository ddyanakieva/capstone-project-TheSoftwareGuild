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
import com.ddyanakieva.blogapp.entities.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @date 28-Aug-2021
 * @author ddyanakieva purpose:
 */
@Controller
public class MainController {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("authors", userDao.getAllUsers());
        model.addAttribute("tags", tagDao.getAllTags());
        return "home";
    }

    @PostMapping("blogsByAuthors")
    public String blogsByAuthors(Integer userId) {
        String redirect = "redirect:/userDetails?userId=" + userId;
        return redirect;
    }

    @GetMapping("blogsByTags")
    public String displayBlogsByTags(int[] tagIds, Model model) {
        if (tagIds.length == 0) {
            return "redirect:/home";
        }
        Set<Blog> blogs = new HashSet<>();
        List<Tag> tags = new ArrayList<>();

        for (int tagId : tagIds) {
            tags.add(tagDao.getTagById(tagId));
        }

        for (Tag tag : tags) {
            List<Blog> listOfBlogsForTag = blogDao.getBlogsForTag(tag);
            for (Blog blog : listOfBlogsForTag) {
                blogs.add(blog);
            }
        }

        model.addAttribute("blogs", blogs);
        model.addAttribute("tags", tags);
        return "blogsByTags";
    }

    @PostMapping("blogsByTags")
    public String blogsByTags(String tagId) {
        if (tagId == null)
            return "redirect:/home";
        String redirect = "redirect:/blogsByTags?tagIds=" + tagId;
        return redirect;
    }

    @GetMapping("blogsByDate")
    public String blogsByDate(String date, Model model) {
        if (date.isEmpty()) {
            return "redirect:/";
        }
        List<Blog> blogs = blogDao.getAllBlogsForDate(LocalDate.parse(date));
        model.addAttribute("blogs", blogs);
        return "blogsByDate";
    }
}
