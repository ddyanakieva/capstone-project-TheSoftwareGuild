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
import com.ddyanakieva.blogapp.entities.Image;
import com.ddyanakieva.blogapp.entities.Tag;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @date 29-Aug-2021
 * @author ddyanakieva purpose:
 */
@Controller
public class ImageController {

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;

    // a class variable to hold the set of ConstraintViolations from our Validator:
    Set<ConstraintViolation<Image>> violations = new HashSet<>();

    @GetMapping("images")
    public String displayImages(Model model) {
        List<Image> images = imageDao.getAllImages();
        model.addAttribute("images", images);
        //the violations variable is added to the Model for the main Images page.
        model.addAttribute("errors", violations);
        return "images";
    }

    @PostMapping("addImage")
    public String addImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String imageFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String imageFolderName = "uploads/";
        // create a new image and store file & folder path
        Image image = new Image();
        image.setImageFolderName(imageFolderName);
        image.setImageFileName(imageFileName);
        // save image to folder
        Path filepath = Paths.get(imageFolderName, imageFileName);

        try {
            multipartFile.transferTo(filepath);
        } catch (IOException e) {
        }
        
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(image);

        if (violations.isEmpty()) {
            imageDao.addImage(image);
        }
        return "redirect:/images";
    }

    @GetMapping("deleteImage")
    public String deleteImage(Integer imageId) {
        Image image = imageDao.getImageById(imageId);
        if (image != null) {
            imageDao.deleteImageById(imageId);
        }
        return "redirect:/images";
    }
}
