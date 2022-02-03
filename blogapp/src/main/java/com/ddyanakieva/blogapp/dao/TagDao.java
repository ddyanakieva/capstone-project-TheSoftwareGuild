/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Tag;
import java.util.List;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
public interface TagDao {
    // basic CRUD operations
    Tag getTagById(int tagId);
    List<Tag> getAllTags();
    Tag addTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTagById(int tagId);
}
