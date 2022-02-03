-- Denitsa Yanakieva (@ddyanakieva)
-- 24/08/2021

DROP DATABASE IF EXISTS denitsayanakievacapstonedb;
CREATE DATABASE denitsayanakievacapstonedb;
USE denitsayanakievacapstonedb;

-- Image table containing the image file names & folder
CREATE TABLE image (
  imageId int not null auto_increment,
  constraint pk_image primary key (imageId),
  imageFileName varchar(100) not null,
  imageFolderName varchar(50) not null
);

-- User table containing user details
CREATE TABLE user (
  userId int not null auto_increment,
  constraint pk_user primary key (userId),
  firstName varchar(50),
  lastName varchar(50) not null,
  isAdmin boolean not null,
  -- profilePic defaults to 1
  profilePic int default 1,
  constraint fk_user_profilePic
    foreign key (profilePic)
    references image(imageId)
);

-- This table stores the authentication validation of users
CREATE TABLE userAuth (
  userId int not null,
  constraint pk_userAuth primary key (userId),
  constraint fk_userAuth
    foreign key (userId)
    references user(userId),
  emailAddress varchar(50) not null,
  password varchar(50) not null
);

-- Tag table containing different tag names
CREATE TABLE tag (
  tagId int not null auto_increment,
  constraint pk_tag primary key (tagId),
  tagName varchar(20) not null,
  -- a hex color code has 6 digits preceded by # sign
  tagColor char(7) DEFAULT "#ffffff"
);

-- the main table of the application
CREATE TABLE blog (
  blogId int not null auto_increment,
  constraint pk_blog primary key (blogId),
  title tinytext not null,
  description text not null,
  dateCreated date not null,
  expirationDate date,
  -- any new post created will need to be approved by the admin first
  -- if created by admin set to true automatically
  isApproved boolean DEFAULT 0
);

-- this table contains the relationship between blog posts and authors.
CREATE TABLE blogUser (
  blogId int not null,
  userId int not null,
  constraint pk_blogUser primary key (blogId, userId),
  constraint fk_blogUser_blog
    foreign key (blogId)
    references blog(blogId),
  constraint fk_blogUser_user
    foreign key (userId)
    references user(userId)
);

-- this table stores all static pages created by admin
CREATE TABLE page (
  pageName varchar(20) not null,
  constraint pk_page primary key (pageName),
  title tinytext not null,
  description mediumtext
);

-- this table holds all tags associated with a blog
CREATE TABLE blogTag (
  blogId int not null,
  tagId int not null,
  constraint pk_blogTag primary key (blogId, tagId),
  constraint fk_blogTag_tag
    foreign key (tagId)
    references tag(tagId),
  constraint fk_blogTag_blog
    foreign key (blogId)
    references blog(blogId)
);

-- this table holds all images associated with a blog
CREATE TABLE blogImage (
  blogId int not null,
  imageId int not null,
  constraint pk_blogImage primary key (blogId, imageId),
  constraint fk_blogImage_image
    foreign key (imageId)
    references image(imageId),
  constraint fk_blogImage_blog
    foreign key (blogId)
    references blog(blogId)
);
