package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {

    @Autowired
    private UserServiceImpl userService;

    @ModelAttribute("currentUserEmailOrUsername")
    public String getCurrentUserEmailOrUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailOrUsername = "";
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                emailOrUsername = ((User) authentication.getPrincipal()).getEmail();
            } else {
                emailOrUsername = authentication.getName();
            }
        }
        return emailOrUsername;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        String emailOrUsername = getCurrentUserEmailOrUsername();
        if (userService.getByEmail(emailOrUsername) != null) {
            return userService.getByEmail(emailOrUsername);
        }
        return userService.getByUsername(emailOrUsername);
    }
}
