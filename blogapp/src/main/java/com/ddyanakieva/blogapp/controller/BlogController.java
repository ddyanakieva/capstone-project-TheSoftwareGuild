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
import com.ddyanakieva.blogapp.entities.Tag;
import com.ddyanakieva.blogapp.entities.User;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @date 27-Aug-2021
 * @author ddyanakieva purpose:
 */
@Controller
public class BlogController {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    // a class variable to hold the set of ConstraintViolations from our Validator:
    Set<ConstraintViolation<Blog>> violations = new HashSet<>();

    @GetMapping("blogs")
    public String displayBlogs(Integer approved, Model model) {
        List<Blog> blogs = blogDao.getActiveBlogs();
        model.addAttribute("blogs", blogs);
        return "blogs";
    }

    @GetMapping("addBlog")
    public String addBlog(Model model) {
        model.addAttribute("users", userDao.getAllUsers());
        model.addAttribute("images", imageDao.getAllImages());
        model.addAttribute("tags", tagDao.getAllTags());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("errors", violations);
        return "addBlog";
    }

    @PostMapping("addBlog")
    public String performAddBlog(Blog blog,
            Integer userId,
            HttpServletRequest request,
            @RequestParam("coverPhoto") MultipartFile multipartFile,
            Model model) throws IOException {

        // try fetching all tags
        // throw an error if no tags selected
        List<Tag> tags = new ArrayList<>();
        String[] tagIds = request.getParameterValues("tagId");
        if (tagIds != null) {
            for (String tagId : tagIds) {
                Tag tag = tagDao.getTagById(Integer.parseInt(tagId));
                tags.add(tag);
            }
        }
        String imageFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String imageFolderName = "uploads/";
        // create a new image and store file & folder path
        Image image = new Image();
        image.setImageFolderName(imageFolderName);
        image.setImageFileName(imageFileName);
        image = imageDao.addImage(image);
        // save image to folder
        Path filepath = Paths.get(imageFolderName, imageFileName);

        try {
            multipartFile.transferTo(filepath);
        } catch (IOException e) {
        }

        List<Image> images = new ArrayList<>();
        images.add(image);
        // add the image to the images of the blog
        blog.setImages(images);

        if (request.getParameter("expiryDate").isEmpty()) {
            blog.setExpirationDate(LocalDate.now());
        } else {
            blog.setExpirationDate(LocalDate.parse(request.getParameter("expiryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        blog.setTags(tags);
        // assign the author associated with the passed userId to the blog author
        blog.setAuthor(userDao.getUserById(userId));
        blog.setDateCreated(LocalDate.now());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(blog);

        if (violations.isEmpty()) {
            blogDao.addBlog(blog);
        }
        return "redirect:/blogs";
    }

    @GetMapping("blogDetails")
    public String displayBlogDetails(Integer blogId, Model model) {
        model.addAttribute("blog", blogDao.getBlogById(blogId));
        return "blogDetails";
    }

    @GetMapping("editBlog")
    public String editBlog(Integer blogId, Model model) {
        Blog blog = blogDao.getBlogById(blogId);
        List<Image> images = imageDao.getAllImages();
        List<Tag> tags = tagDao.getAllTags();
        List<User> users = userDao.getAllUsers();
        model.addAttribute("blog", blog);
        model.addAttribute("images", images);
        model.addAttribute("tags", tags);
        model.addAttribute("authors", users);
        return "editBlog";
    }

    @PostMapping("editBlog")
    public String performEditBlog(@Valid Blog blog, BindingResult result,
            Integer userId, Integer imageId, HttpServletRequest request, Model model,
            @RequestParam("coverPhoto") MultipartFile multipartFile) throws IOException {

        // assign the author associated with the passed userId to the blog author
        blog.setAuthor(userDao.getUserById(userId));
        blog.setDateCreated(LocalDate.now());

        // try fetching all tags
        // throw an error if no tags selected
        List<Tag> tags = new ArrayList<>();
        String[] tagIds = request.getParameterValues("tagId");

        if (tagIds != null) {
            for (String tagId : tagIds) {
                Tag tag = tagDao.getTagById(Integer.parseInt(tagId));
                tags.add(tag);
            }
        } else {
            FieldError error = new FieldError("blog", "tags", "Must include at least one tag");
            result.addError(error);
        }
        blog.setTags(tags);

        Image image = new Image();
        if (multipartFile.isEmpty()) {
            image = imageDao.getImageById(imageId);
        } else {
            String imageFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String imageFolderName = "blog-cover-photos/";
            // create a new image and store file & folder path
            image.setImageFolderName(imageFolderName);
            image.setImageFileName(imageFileName);
            // save image to folder
            Path filepath = Paths.get(imageFolderName, imageFileName);

            try {
                multipartFile.transferTo(filepath);
                image = imageDao.addImage(image);
            } catch (IOException e) {
                FieldError error = new FieldError("blog", "coverPhoto", "Couldn't save image to files.");
                result.addError(error);
            }
        }

        List<Image> images = new ArrayList<>();
        images.add(image);
        // add the image to the images of the blog
        blog.setImages(images);

        if (request.getParameter("expiryDate").isEmpty()) {
            blog.setExpirationDate(LocalDate.now());
        } else {
            blog.setExpirationDate(LocalDate.parse(request.getParameter("expiryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        if (result.hasErrors()) {
            imageDao.deleteImageById(image.getImageId());
            model.addAttribute("blog", blog);
            model.addAttribute("images", imageDao.getAllImages());
            model.addAttribute("tags", tagDao.getAllTags());
            model.addAttribute("authors", userDao.getAllUsers());
        }
        blogDao.updateBlog(blog);
        return "redirect:/blogs";
    }

    @GetMapping("deleteBlog")
    public String deleteBlog(Integer blogId) {
        blogDao.deleteBlogById(blogId);
        return "redirect:/blogs";
    }
}
