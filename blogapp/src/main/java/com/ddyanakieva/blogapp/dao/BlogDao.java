/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Blog;
import com.ddyanakieva.blogapp.entities.Tag;
import com.ddyanakieva.blogapp.entities.User;
import java.time.LocalDate;
import java.util.List;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
public interface BlogDao {
    Blog getBlogById(int blogId);
    List<Blog> getAllBlogsForDate(LocalDate dateCreated);
    List<Blog> getAllBlogs();
    List<Blog> getUnapprovedBlogs();
    List<Blog> getExpiredBlogs();
    List<Blog> getActiveBlogs();
    Blog addBlog(Blog blog);
    void updateBlog(Blog blog);
    void deleteBlogById(int blogId);

    List<Blog> getBlogsForUser(User user);
    List<Blog> getBlogsForTag(Tag tag);
}
