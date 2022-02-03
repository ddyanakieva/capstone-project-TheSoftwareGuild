/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.entities;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */

public class Tag {
    private int tagId;
    @NotBlank(message = "The tag name must not be empty.")
    @Size(max = 20, message = "The tag name must be less than 20 characters.")
    private String tagName;
    @Size(min = 6,max = 7, message = "The colour must be in standard hex format.")
    private String tagColor;    
        
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
    
    public int getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.tagId;
        hash = 67 * hash + Objects.hashCode(this.tagName);
        hash = 67 * hash + Objects.hashCode(this.tagColor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        if (this.tagId != other.tagId) {
            return false;
        }
        if (!Objects.equals(this.tagName, other.tagName)) {
            return false;
        }
        if (!Objects.equals(this.tagColor, other.tagColor)) {
            return false;
        }
        return true;
    } 
}
