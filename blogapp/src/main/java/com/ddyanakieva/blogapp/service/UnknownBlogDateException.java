/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.service;

/**
 * @date 25-Aug-2021
 * @author ddyanakieva
 * purpose:
 */
public class UnknownBlogDateException extends Exception{
    public UnknownBlogDateException(String message){
        super(message);
    }
    public UnknownBlogDateException(String message, Throwable error){
        super(message, error);
    }
}
