/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Blog;
import com.ddyanakieva.blogapp.entities.Image;
import com.ddyanakieva.blogapp.entities.Tag;
import com.ddyanakieva.blogapp.entities.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author ddyanakieva
 */
@SpringBootTest
public class UserDaoDBTest {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    public UserDaoDBTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    // remove all pre-existing objects
    // that have been created during testing
    public void setUp() {
        List<Tag> tags = tagDao.getAllTags();
        for (Tag tag : tags) {
            tagDao.deleteTagById(tag.getTagId());
        }
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            userDao.deleteUserById(user.getUserId());
        }

        List<Image> images = imageDao.getAllImages();
        for (Image image : images) {
            imageDao.deleteImageById(image.getImageId());
        }

        List<Blog> blogs = blogDao.getAllBlogs();
        for (Blog blog : blogs) {
            blogDao.deleteBlogById(blog.getBlogId());
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddAndGetUser() {
        Image image = new Image();
        image.setImageFileName("");
        image.setImageFolderName("");
        image = imageDao.addImage(image);

        User user = new User();
        user.setFirstName("Denitsa");
        user.setLastName("Yanakieva");
        user.setIsAdmin(true);
        user.setProfilePic(image);
        user = userDao.addUser(user);

        User fromDao = userDao.getUserById(user.getUserId());
        assertEquals(fromDao, user);
    }

    @Test
    public void testUpdateUser() {
        Image image = new Image();
        image.setImageFileName("");
        image.setImageFolderName("");
        image = imageDao.addImage(image);
        List<Image> images = new ArrayList<>();
        images.add(image);

        User user = new User();
        user.setFirstName("Denitsa");
        user.setLastName("Yanakieva");
        user.setIsAdmin(true);
        user.setProfilePic(image);
        user = userDao.addUser(user);

        User fromDao = userDao.getUserById(user.getUserId());
        assertEquals(fromDao, user);

        user.setFirstName("Deni");
        userDao.updateUser(user);
        assertFalse(fromDao.equals(user));

        fromDao = userDao.getUserById(user.getUserId());
        assertEquals(fromDao, user);
    }

    @Test
    public void testGetAllUsers() {
        Image image = new Image();
        image.setImageFileName("");
        image.setImageFolderName("");
        image = imageDao.addImage(image);
        List<Image> images = new ArrayList<>();
        images.add(image);

        User user1 = new User();
        user1.setFirstName("Denitsa");
        user1.setLastName("Yanakieva");
        user1.setIsAdmin(true);
        user1.setProfilePic(image);
        user1 = userDao.addUser(user1);

        User user2 = new User();
        user2.setFirstName("Denitsa 2");
        user2.setLastName("Yanakieva 2");
        user2.setIsAdmin(false);
        user2.setProfilePic(image);
        user2 = userDao.addUser(user2);

        List<User> users = userDao.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testDeleteUser() {
        Tag tag = new Tag();
        tag.setTagName("Cool tag");
        tag.setTagColor("#FFFFFF");
        tag = tagDao.addTag(tag);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);

        Image image = new Image();
        image.setImageFileName("");
        image.setImageFolderName("");
        image = imageDao.addImage(image);
        List<Image> images = new ArrayList<>();
        images.add(image);

        User user = new User();
        user.setFirstName("Denitsa");
        user.setLastName("Yanakieva");
        user.setIsAdmin(true);
        user.setProfilePic(image);
        user = userDao.addUser(user);

        Blog blog = new Blog();
        blog.setDateCreated(LocalDate.now());
        blog.setDescription("");
        blog.setTitle("");
        blog.setIsApproved(true);
        blog.setExpirationDate(LocalDate.now());
        blog.setAuthor(user);
        blog.setTags(tags);
        blog.setImages(images);
        blogDao.addBlog(blog);

        User fromDao = userDao.getUserById(user.getUserId());
        assertEquals(fromDao, user);

        userDao.deleteUserById(user.getUserId());

        fromDao = userDao.getUserById(user.getUserId());
        assertNull(fromDao);
    }
}
