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
public class TagDaoDBTest {

    @Autowired
    BlogDao blogDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ImageDao imageDao;

    public TagDaoDBTest() {
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
        for (Image image : images){
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
    public void testAddAndGetTag() {
        Tag tag = new Tag();
        tag.setTagName("Cool tag");
        tag.setTagColor("#FFFFFF");
        tag = tagDao.addTag(tag);

        Tag fromDao = tagDao.getTagById(tag.getTagId());
        assertEquals(fromDao, tag);
    }

    @Test
    public void testGetAllTags() {
        Tag tag1 = new Tag();
        tag1.setTagName("Cool tag");
        tag1.setTagColor("#FFFFFF");
        tag1 = tagDao.addTag(tag1);

        Tag tag2 = new Tag();
        tag2.setTagName("Super tag");
        tag2.setTagColor("#000000");
        tag2 = tagDao.addTag(tag2);

        List<Tag> tags = tagDao.getAllTags();
        assertEquals(2, tags.size());
        assertNotNull(tags);
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }

    @Test
    public void testUpdateTag() {
        Tag tag = new Tag();
        tag.setTagName("Cool tag");
        tag.setTagColor("#FFFFFF");
        tag = tagDao.addTag(tag);

        Tag fromDao = tagDao.getTagById(tag.getTagId());
        assertEquals(fromDao, tag);

        tag.setTagColor("#000000");
        tagDao.updateTag(tag);

        assertNotEquals(fromDao, tag);

        fromDao = tagDao.getTagById(tag.getTagId());
        assertEquals(fromDao, tag);
    }

    @Test
    public void testDeleteTagById() {
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

        Tag fromDao = tagDao.getTagById(tag.getTagId());
        assertEquals(fromDao, tag);

        tagDao.deleteTagById(tag.getTagId());

        fromDao = tagDao.getTagById(tag.getTagId());
        assertNull(fromDao);
    }
}
