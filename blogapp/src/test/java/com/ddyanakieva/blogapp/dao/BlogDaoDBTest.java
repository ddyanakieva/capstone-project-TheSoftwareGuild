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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author ddyanakieva
 */
@SpringBootTest
public class BlogDaoDBTest {

    @Autowired
    BlogDao blogDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ImageDao imageDao;

    public BlogDaoDBTest() {
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
    public void testAddAndGetBlog() {
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
        blog = blogDao.addBlog(blog);

        Blog fromDao = blogDao.getBlogById(blog.getBlogId());
////        Debugging purposes only.
//        assertEquals(fromDao.getBlogId(), blog.getBlogId());
//        assertEquals(fromDao.getDateCreated(), blog.getDateCreated());
//        assertEquals(fromDao.getDescription(), blog.getDescription());
//        assertEquals(fromDao.getExpirationDate(), blog.getExpirationDate());
//        assertEquals(fromDao.getImages(), blog.getImages());
//        assertEquals(fromDao.getTags(), blog.getTags());
//        assertEquals(fromDao.getTitle(), blog.getTitle());
//
//        assertEquals(fromDao.getAuthor().getUserId(), blog.getAuthor().getUserId());
//        assertEquals(fromDao.getAuthor().getFirstName(), blog.getAuthor().getFirstName());
//        assertEquals(fromDao.getAuthor().getLastName(), blog.getAuthor().getLastName());
//        assertEquals(fromDao.getAuthor().getProfilePic(), blog.getAuthor().getProfilePic());
//        assertEquals(fromDao.getAuthor().isIsAdmin(), blog.getAuthor().isIsAdmin());
        assertEquals(blog, fromDao);
    }

    @Test
    public void testGetAllBlogs() {
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

        Blog blog1 = new Blog();
        blog1.setDateCreated(LocalDate.now());
        blog1.setDescription("");
        blog1.setTitle("");
        blog1.setIsApproved(true);
        blog1.setExpirationDate(LocalDate.now());
        blog1.setAuthor(user);
        blog1.setTags(tags);
        blog1.setImages(images);
        blog1 = blogDao.addBlog(blog1);

        Blog blog2 = new Blog();
        blog2.setDateCreated(LocalDate.now());
        blog2.setDescription("");
        blog2.setTitle("");
        blog2.setIsApproved(true);
        blog2.setExpirationDate(LocalDate.now());
        blog2.setAuthor(user);
        blog2.setTags(tags);
        blog2.setImages(images);
        blog2 = blogDao.addBlog(blog2);

        List<Blog> blogs = blogDao.getAllBlogs();
        assertEquals(2, blogs.size());
        assertTrue(blogs.contains(blog1));
        assertTrue(blogs.contains(blog2));
    }

    @Test
    public void testDeleteBlog() {
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
        blog = blogDao.addBlog(blog);

        Blog fromDao = blogDao.getBlogById(blog.getBlogId());
        assertEquals(fromDao, blog);

        blogDao.deleteBlogById(blog.getBlogId());
        fromDao = blogDao.getBlogById(blog.getBlogId());

        assertNull(fromDao);
    }

    @Test
    public void testUpdateBlogItself() {
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
        blog = blogDao.addBlog(blog);

        Blog fromDao = blogDao.getBlogById(blog.getBlogId());
        blog.setDescription("new description");
        blogDao.updateBlog(blog);
        assertFalse(fromDao.equals(blog));

        fromDao = blogDao.getBlogById(blog.getBlogId());
        assertEquals(fromDao, blog);
    }

    @Test
    public void testUpdateBlogAndImage() {
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
        blog = blogDao.addBlog(blog);
        Blog fromDao = blogDao.getBlogById(blog.getBlogId());

        // update the image with new details
        image.setImageFileName("newImage.png");
        // changing any of the details of the image object
        // should update the database as well
        imageDao.updateImage(image);
        // clear the list and add the new updated image
        images.clear();
        images.add(image);
        // set the list of images back to the blog object
        blog.setImages(images);
        // update the blog object
        blogDao.updateBlog(blog);
        // the object fetched before the update and the newly updated 
        // blog object should not be equals
        assertFalse(fromDao.equals(blog));

        // however, if we fetch the blog here
        fromDao = blogDao.getBlogById(blog.getBlogId());
        // equality assertion should pass
        assertEquals(fromDao, blog);
    }

    @Test
    public void testUpdateBlogAndUser() {
        // This test folows the same structure as testUpdateBlogAndImage
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
        blog = blogDao.addBlog(blog);
        Blog fromDao = blogDao.getBlogById(blog.getBlogId());

        // change the user & update database
        user.setFirstName("Deni");
        userDao.updateUser(user);
        // supdate the blog & dao
        blog.setAuthor(user);
        blogDao.updateBlog(blog);
        assertFalse(fromDao.equals(blog));

        fromDao = blogDao.getBlogById(blog.getBlogId());
        assertEquals(fromDao, blog);
    }

    @Test
    public void testUpdateBlogAndTag() {
        // this test follows the same structure as testUpdateBlogAndImage
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
        blog = blogDao.addBlog(blog);
        Blog fromDao = blogDao.getBlogById(blog.getBlogId());
        
        tag.setTagName("Better tag");
        tagDao.updateTag(tag);
        tags.clear();
        tags.add(tag);
        blog.setTags(tags);
        blogDao.updateBlog(blog);
        assertFalse(fromDao.equals(blog));
        
        fromDao = blogDao.getBlogById(blog.getBlogId());
        assertEquals(fromDao, blog);
    }
    
    @Test
    public void testGetBlogsForUser(){
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

        Blog blog = new Blog();
        blog.setDateCreated(LocalDate.now());
        blog.setDescription("");
        blog.setTitle("");
        blog.setIsApproved(true);
        blog.setExpirationDate(LocalDate.now());
        blog.setAuthor(user1);
        blog.setTags(tags);
        blog.setImages(images);
        blog = blogDao.addBlog(blog);
        
        List<Blog> blogs = blogDao.getBlogsForUser(user1);
        assertTrue(blogs.contains(blog));
        blogs = blogDao.getBlogsForUser(user2);
        assertFalse(blogs.contains(blog));
    }
    
    @Test
    public void testGetBlogsForTags(){
        
    }
}
