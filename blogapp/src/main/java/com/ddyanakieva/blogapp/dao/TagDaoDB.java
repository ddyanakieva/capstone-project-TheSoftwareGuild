/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
@Repository
public class TagDaoDB implements TagDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Tag getTagById(int tagId) {
        // sql prepared statement
        final String SELECT_TAG_ID = "SELECT * FROM tag WHERE tagId = ?";
        try{
            Tag tag = jdbc.queryForObject(SELECT_TAG_ID, new TagMapper(), tagId);
            return tag;
        }catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Tag> getAllTags() {
        // sql statement
        final String SELECT_ALL_TAGS = "SELECT * FROM tag";
        return jdbc.query(SELECT_ALL_TAGS, new TagMapper());
    }

    @Override
    @Transactional
    // '@Transcational' annotation ensures that 
    // if any of the queries fail (throw an exception)
    // the whole operation (method) will terminate
    public Tag addTag(Tag tag) {
        final String INSERT_TAG = "INSERT INTO tag(tagName, tagColor) VALUES (?,?)";
        jdbc.update(INSERT_TAG, tag.getTagName(), tag.getTagColor());
        // fetch the auto incremented id
        int tagId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        tag.setTagId(tagId);
        return tag;
    }

    @Override
    public void updateTag(Tag tag) {
        final String UPDATE_TAG = "UPDATE tag SET tagName = ?, tagColor = ? "
                + "WHERE tagId = ?";
        jdbc.update(UPDATE_TAG,
                tag.getTagName(),
                tag.getTagColor(),
                tag.getTagId());
    }

    @Override
    @Transactional
    public void deleteTagById(int tagId) {
        // delete from foreign tables first which reference the primary key -- tagId
        final String DELETE_FROM_BLOGTAG = "DELETE FROM blogTag WHERE tagId = ?";
        jdbc.update(DELETE_FROM_BLOGTAG, tagId);
        
        // delete from main table
        final String DELETE_FROM_TAG = "DELETE FROM tag WHERE tagId = ?";
        jdbc.update(DELETE_FROM_TAG, tagId);
    }
    
    public static final class TagMapper implements RowMapper<Tag>{

        @Override
        public Tag mapRow(ResultSet rs, int i) throws SQLException {
            Tag tag = new Tag();
            tag.setTagName(rs.getString("tagName"));
            tag.setTagId(rs.getInt("tagId"));
            tag.setTagColor(rs.getString("tagColor"));
            return tag;
        }
        
    }
}
