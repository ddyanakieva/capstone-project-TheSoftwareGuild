/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.dao.ImageDaoDB.ImageMapper;
import com.ddyanakieva.blogapp.dao.TagDaoDB.TagMapper;
import com.ddyanakieva.blogapp.dao.UserDaoDB.UserMapper;
import com.ddyanakieva.blogapp.entities.Blog;
import com.ddyanakieva.blogapp.entities.Image;
import com.ddyanakieva.blogapp.entities.Tag;
import com.ddyanakieva.blogapp.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class BlogDaoDB implements BlogDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Blog getBlogById(int blogId) {
        try {
            final String SELECT_BLOG_BY_ID = "SELECT * FROM blog WHERE blogId = ?";
            Blog blog = jdbc.queryForObject(SELECT_BLOG_BY_ID, new BlogMapper(), blogId);
            //retrieve the associated user -- author
            blog.setAuthor(getAuthorForBlog(blogId));
            // retrieve the list of associated tags for a blog
            blog.setTags(getTagsForBlog(blogId));
            // retrieve the list of images contained in the blog
            blog.setImages(getImagesForBlog(blogId));
            
            return blog;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Blog> getAllBlogsForDate(LocalDate dateCreated) {
        final String SELECT_BLOG_BY_DATE = "SELECT * FROM blog WHERE dateCreate = ?";
        try {
            List<Blog> blogs = jdbc.query(SELECT_BLOG_BY_DATE, new BlogMapper(), dateCreated);
            this.associateAuthor_Tags_Images(blogs);
            return blogs;
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Blog> getAllBlogs() {
        final String SELECT_ALL_BLOGS = "SELECT * FROM blog";
        List<Blog> blogs = jdbc.query(SELECT_ALL_BLOGS, new BlogMapper());
        this.associateAuthor_Tags_Images(blogs);
        return blogs;
    }

    @Override
    public List<Blog> getUnapprovedBlogs() {
        final String SELECT_UNAPPROVED_BLOGS = " SELECT * FROM blog WHERE isApproved = false";
        List<Blog> blogs = jdbc.query(SELECT_UNAPPROVED_BLOGS, new BlogMapper());
        this.associateAuthor_Tags_Images(blogs);
        return blogs;
    }
    
    @Override
    public List<Blog> getExpiredBlogs() {
        final String SELECT_EXPIRED_BLOGS = "SELECT * FROM blog "
                + "WHERE expirationDate != dateCreated AND expirationDate < ? "
                + "ORDER BY dateCreated DESC";
        List<Blog> blogs = jdbc.query(SELECT_EXPIRED_BLOGS, new BlogMapper(), LocalDate.now());
        this.associateAuthor_Tags_Images(blogs);
        return blogs;
    }
    
    @Override
    public List<Blog> getActiveBlogs() {
        final String SELECT_EXPIRED_BLOGS = "SELECT * FROM blog "
                + "WHERE expirationDate = dateCreated OR (expirationDate > ?) "
                + "ORDER BY dateCreated ASC";
        List<Blog> blogs = jdbc.query(SELECT_EXPIRED_BLOGS, new BlogMapper(), LocalDate.now());
        this.associateAuthor_Tags_Images(blogs);
        return blogs;
    }

    // helper method
    private User getAuthorForBlog(int blogId) {
        final String SELECT_AUTHOR_FOR_BLOG = "SELECT * FROM user u "
                + "JOIN blogUser bu ON bu.userId = u.userId "
                + "WHERE blogId = ?";
        try {
            User user = jdbc.queryForObject(SELECT_AUTHOR_FOR_BLOG, new UserMapper(), blogId);
            // need to fetch and associate an Image object with User
            // a User object is composite of an Image object
            // retrieving related fields needs to propagate to the Image object as well
            user.setProfilePic(getImageForUser(user.getUserId()));
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    // helper method
    private Image getImageForUser(int userId){
        final String SELECT_IMAGE_FOR_USER = "SELECT * FROM image "
                + "JOIN user ON profilePic = imageId "
                + "WHERE userId = ?";
        return jdbc.queryForObject(SELECT_IMAGE_FOR_USER, new ImageMapper(), userId);
    }

    // helper method
    private List<Tag> getTagsForBlog(int blogId) {
        final String SELECT_TAGS_FOR_BLOG = "SELECT * FROM tag "
                + "JOIN blogTag ON blogTag.tagId = tag.tagId "
                + "WHERE blogTag.blogId = ?";
        try {
            return jdbc.query(SELECT_TAGS_FOR_BLOG, new TagMapper(), blogId);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    // helper method
    private List<Image> getImagesForBlog(int blogId) {
        final String SELECT_IMAGE_FOR_BLOG = "SELECT * FROM image i "
                + "JOIN blogImage b ON i.imageId = b.imageId "
                + "WHERE b.blogId = ?";
        try {
            return jdbc.query(SELECT_IMAGE_FOR_BLOG, new ImageMapper(), blogId);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    // helper method
    private void associateAuthor_Tags_Images(List<Blog> blogs) {
        // for each blog in the list, associate a User object -- author
        // and a list of Tag objects
        for (Blog blog : blogs) {
            blog.setTags(this.getTagsForBlog(blog.getBlogId()));
            blog.setAuthor(this.getAuthorForBlog(blog.getBlogId()));
            blog.setImages(this.getImagesForBlog(blog.getBlogId()));
        }
    }

    @Override
    @Transactional
    public Blog addBlog(Blog blog) {
        final String INSERT_BLOG = "INSERT INTO "
                + "blog(title, description, dateCreated, isApproved, expirationDate) "
                + "VALUES (?,?,?,?,?)";
        jdbc.update(INSERT_BLOG,
                blog.getTitle(),
                blog.getDescription(),
                blog.getDateCreated(),
                blog.isIsApproved(),
                blog.getExpirationDate());

        int blogId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        blog.setBlogId(blogId);

        // we only need to insert the tags, author, images & expiration in the database
        // not necessarily in the Blog object itself 
        // therefor, the following is not needed:
        // blog.setTags(this.getTagsForBlog(blogId)); , etc
        // as those will be created by another component of the MVC app -- i.e View
        this.insertBlogAuthor(blog);
        this.insertBlogImages(blog);
        this.insertBlogTags(blog);
        return blog;
    }

    // helper method
    private void insertBlogTags(Blog blog) {
        final String INSERT_BLOG_TAGS = "INSERT INTO blogTag(blogId, tagId) VALUES (?,?)";
        for (Tag tag : blog.getTags()) {
            jdbc.update(INSERT_BLOG_TAGS,
                    blog.getBlogId(),
                    tag.getTagId());
        }
    }

    // helper method
    private void insertBlogAuthor(Blog blog) {
        final String INSERT_BLOG_AUTHOR = "INSERT INTO blogUser(blogId, userId) VALUES (?,?)";
        jdbc.update(INSERT_BLOG_AUTHOR,
                blog.getBlogId(),
                blog.getAuthor().getUserId());
    }

    // helper method
    private void insertBlogImages(Blog blog) {
        final String INSERT_BLOG_IMAGES = "INSERT INTO blogImage(blogId, imageId) VALUES (?,?)";
        for (Image image : blog.getImages()) {
            jdbc.update(INSERT_BLOG_IMAGES,
                    blog.getBlogId(),
                    image.getImageId());
        }
    }

    @Override
    @Transactional
    // this time the update method is transactional
    // because we need to update multiple entries in the DB
    // containing the primary key of a blog as a foreign key
    // i.e in the blogTag, blogImage, blogAuthor, blogExpiration tables
    public void updateBlog(Blog blog) {
        // we need to update all possible fields
        // including records in the the related tables
        // because we do not know which exact attribute 
        // the user wants to edit
        final String UPDATE_BLOG = "UPDATE blog SET title = ?, description = ?, "
                + "dateCreated = ?, isApproved = ?, expirationDate = ? "
                + "WHERE blogId = ?";
        jdbc.update(UPDATE_BLOG,
                blog.getTitle(),
                blog.getDescription(),
                blog.getDateCreated(),
                blog.isIsApproved(),
                blog.getExpirationDate(),
                blog.getBlogId());
        // updating the list of tags associated with a blog id
        // is done by first deleting all instances of the blog id related to a tag id
        // similarly for the other relations.
        final String DELETE_BLOG_TAG = "DELETE FROM blogTag WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_TAG, blog.getBlogId());
        final String DELETE_BLOG_AUTHOR = "DELETE FROM blogUser WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_AUTHOR, blog.getBlogId());
        final String DELETE_BLOG_IMAGE = "DELETE FROM blogImage WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_IMAGE, blog.getBlogId());

        // and then create the new relations as well
        // in case any of them gets updated as well
        this.insertBlogAuthor(blog);
        this.insertBlogImages(blog);
        this.insertBlogTags(blog);
    }

    @Override
    @Transactional
    public void deleteBlogById(int blogId) {
        // first delete any entries in foreign tables
        // referencing the primary key -- blog id
        final String DELETE_BLOG_TAG = "DELETE FROM blogTag WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_TAG, blogId);
        final String DELETE_BLOG_AUTHOR = "DELETE FROM blogUser WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_AUTHOR, blogId);
        final String DELETE_BLOG_IMAGE = "DELETE FROM blogImage WHERE blogId = ?";
        jdbc.update(DELETE_BLOG_IMAGE, blogId);

        // remove entry from main table
        final String DELETE_BLOG = "DELETE FROM blog WHERE blogId = ?";
        jdbc.update(DELETE_BLOG, blogId);
    }

    @Override
    public List<Blog> getBlogsForUser(User user) {
        final String SELECT_BLOGS_FOR_USER = "SELECT * FROM blog b "
                + "JOIN blogUser bu ON b.blogId = bu.blogId "
                + "WHERE bu.userId = ?";        
        try {
            List<Blog> blogs = jdbc.query(SELECT_BLOGS_FOR_USER, new BlogMapper(), user.getUserId());
            this.associateAuthor_Tags_Images(blogs);
            return blogs;
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Blog> getBlogsForTag(Tag tag) {
        final String SELECT_BLOGS_FOR_TAG = "SELECT b.* FROM blog b "
                + "JOIN blogTag bt ON b.blogId = bt.blogId "
                + "WHERE bt.tagId = ?";
        
        try {
            List<Blog> blogs = jdbc.query(SELECT_BLOGS_FOR_TAG, new BlogMapper(), tag.getTagId());
            this.associateAuthor_Tags_Images(blogs);
            return blogs;
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    public final static class BlogMapper implements RowMapper<Blog> {

        @Override
        public Blog mapRow(ResultSet rs, int i) throws SQLException {
            // cannot fetch images, tags & author here
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");            
            LocalDate dateCreated = LocalDate.parse(rs.getString("dateCreated"), formatter);
            Blog blog = new Blog();
            blog.setDateCreated(dateCreated);
            blog.setBlogId(rs.getInt("blogId"));
            blog.setDescription(rs.getString("description"));
            blog.setIsApproved(rs.getBoolean("isApproved"));
            blog.setTitle(rs.getString("title"));
            LocalDate expirationDate = LocalDate.parse(rs.getString("expirationDate"), formatter);
            blog.setExpirationDate(expirationDate);
            return blog;
        }
    }
}
