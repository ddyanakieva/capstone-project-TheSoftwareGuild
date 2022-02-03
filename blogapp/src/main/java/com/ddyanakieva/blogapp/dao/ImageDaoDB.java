/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Image;
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
 * @date 26-Aug-2021
 * @author ddyanakieva purpose:
 */
@Repository
public class ImageDaoDB implements ImageDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Image addImage(Image image) {
        jdbc.update("INSERT INTO image(imageFolderName, imageFileName) VALUES(?,?)",
                image.getImageFolderName(), image.getImageFileName());
        int imageId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        image.setImageId(imageId);
        return image;
    }

    @Override
    public Image getImageById(int imageId) {
        try {
            return jdbc.queryForObject("SELECT * FROM image WHERE imageId = ?", new ImageMapper(), imageId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Image> getAllImages() {
        return jdbc.query("SELECT * FROM image", new ImageMapper());
    }

    @Override
    @Transactional
    public void deleteImageById(int imageId) {
        jdbc.update("DELETE FROM blogImage WHERE imageId = ?", imageId);
        jdbc.update("DELETE FROM image WHERE imageId = ?", imageId);
    }

    @Override
    public void updateImage(Image image) {
        jdbc.update("UPDATE image SET imageFileName = ?, imageFolderName = ? "
                + "WHERE imageId = ?",
                image.getImageFileName(),
                image.getImageFolderName(),
                image.getImageId());
    }

    public final static class ImageMapper implements RowMapper<Image> {

        @Override
        public Image mapRow(ResultSet rs, int i) throws SQLException {
            Image image = new Image();
            image.setImageId(rs.getInt("imageId"));
            image.setImageFolderName(rs.getString("imageFolderName"));
            image.setImageFileName(rs.getString("imageFileName"));
            return image;
        }

    }

}
