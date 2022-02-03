/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddyanakieva.blogapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @date 28-Aug-2021
 * @author ddyanakieva purpose:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
        "/addBlog","/editBlog","/editTag","/users","tags"};

    @Autowired
    public void configureGlobalInMemory(AuthenticationManagerBuilder auth) throws Exception {
        //The '{noop}' in the password is there to tell Spring 
        // to not try to use a password encoder for this authentication, the password is still just 'password'
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // `antMatchers` to match against paths into our application
        // and indicating the type of security for that path
        // The `hasRole` security method limits to a specific role
        // while `permitAll` lets everyone access a path
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/dashboard","/editUser").hasRole("ADMIN")
                .antMatchers(AUTH_WHITELIST).authenticated()
                .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
                .antMatchers("/csrf").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?login_error=1")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home?logout")
                .permitAll();

    }
}
