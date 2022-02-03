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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @date 28-Aug-2021
 * @author ddyanakieva purpose:
 */
@Controller
public class AdminController {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    @GetMapping("dashboard")
    public String showDashboard(Model model) {
        List<Blog> unapprovedBlogs = blogDao.getUnapprovedBlogs();
        List<Blog> expiredBlogs = blogDao.getExpiredBlogs();
        model.addAttribute("unapprovedBlogs", unapprovedBlogs);
        model.addAttribute("expiredBlogs", expiredBlogs);
        return "dashboard";
    }

    @PostMapping("approveBlog")
    public String approveBlog(Integer blogId) {
        Blog blog = blogDao.getBlogById(blogId);
        if (blog != null) {
            blog.setIsApproved(true);
            blogDao.updateBlog(blog);
            return "redirect:/dashboard?successApprove";
        }
        return "redirect:/dashboard?failApprove";
    }
}
