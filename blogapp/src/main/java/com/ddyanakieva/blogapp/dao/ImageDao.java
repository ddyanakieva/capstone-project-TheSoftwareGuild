/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.dao;

import com.ddyanakieva.blogapp.entities.Image;
import java.util.List;

/**
 * @date 26-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
public interface ImageDao {
    Image addImage(Image image);
    Image getImageById (int imageId);
    List<Image> getAllImages();
    void deleteImageById (int imageId);
    void updateImage (Image image);
}
