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
import com.ddyanakieva.blogapp.entities.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @date 26-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
@Controller
public class TagController {
    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ImageDao imageDao;
    
    // a class variable to hold the set of ConstraintViolations from our Validator:
    Set<ConstraintViolation<Tag>> violations = new HashSet<>();
    
    @GetMapping("tags")
    public String displayTags(Model model){
        List<Tag> tags = tagDao.getAllTags();
        model.addAttribute("tags", tags);
        //the violations variable is added to the Model for the main Tags page.
        model.addAttribute("errors", violations);
        return "tags";
    }
        
    @PostMapping("addTags")
    public String addTags(@Valid Tag tag, BindingResult result) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(tag);
        
        if (violations.isEmpty()) {        
            tagDao.addTag(tag);
        }
        return "redirect:/tags";
    }
    
    @GetMapping("editTag")
    public String editTag(Integer tagId, Model model){
        Tag tag = tagDao.getTagById(tagId);
        model.addAttribute("tag", tag);
        return "editTag";
    }
    
    @PostMapping("editTag")
    public String performEditTag(@Valid Tag tag, BindingResult result){
        if(result.hasErrors()){
           return "editTag"; 
        }
        tagDao.updateTag(tag);
        return "redirect:/tags";
    }
    
    @GetMapping("deleteTag")
    public String deleteTag(Integer tagId, Model model){
        tagDao.deleteTagById(tagId);
        return "redirect:/tags";
    }
    
}
