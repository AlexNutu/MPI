package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirectToLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/home";
    }//redirect:login

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(HttpServletRequest request, Model model) {
        return "login";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String successRedirect() throws NoSuchAlgorithmException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities();
        String emailOrUsername;
        if (authentication.getPrincipal() instanceof User) {
            emailOrUsername = ((User) authentication.getPrincipal()).getEmail();
        } else {
            emailOrUsername = authentication.getName();
        }
        User user = null;
        if (userService.getByEmail(emailOrUsername) != null) {
            user = userService.getByEmail(emailOrUsername);
        } else {
            user = userService.getByUsername(emailOrUsername);
        }

        // 0 admin, 1 user
        if (user.getRole() == 0) {
            return "redirect:/admin/home";
        } else {//if (user.getRole() == 1) {
            return "redirect:/user/home";
        }
        //return "redirect:/error";
    }

    @ExceptionHandler()
    public ModelAndView error(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e);
        mav.setViewName("error");

        return mav;
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getCurrentUser(HttpServletRequest request) {
        return request.getUserPrincipal().getName();
    }

}
