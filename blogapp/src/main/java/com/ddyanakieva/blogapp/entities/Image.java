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
 * @author ddyanakieva purpose:
 */
public class Image {

    private int imageId;
    @NotBlank(message = "File name cannot be blank.")
    @Size(max = 100, message = "File name should not exceed 100 characters.")
    private String imageFileName;
    @Size(max = 100, message = "Folder name should not exceed 100 characters.")
    private String imageFolderName;
    
    public Image(){
        imageFolderName = "";
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFolderName() {
        return imageFolderName;
    }

    public void setImageFolderName(String imageFolderName) {
        this.imageFolderName = imageFolderName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.imageId;
        hash = 97 * hash + Objects.hashCode(this.imageFileName);
        hash = 97 * hash + Objects.hashCode(this.imageFolderName);
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
        final Image other = (Image) obj;
        if (this.imageId != other.imageId) {
            return false;
        }
        if (!Objects.equals(this.imageFileName, other.imageFileName)) {
            return false;
        }
        if (!Objects.equals(this.imageFolderName, other.imageFolderName)) {
            return false;
        }
        return true;
    }
    
    
}
