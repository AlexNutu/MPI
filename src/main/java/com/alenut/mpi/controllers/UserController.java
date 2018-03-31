package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    UserService userService;

    @Autowired
    IdeaServiceImpl ideaService;

    @RequestMapping(value = "/editUser", method = RequestMethod.POST,
            consumes = {"application/json"})
    public String editUser(@RequestBody User userInfo, Model model) throws NoSuchAlgorithmException {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());

        userService.editUser(userInfo);

        return "manageUser";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/user/home";
    }


    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public ModelAndView createUserView(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        ModelAndView modelAndView = new ModelAndView("createUser");
        modelAndView.addObject("user", new User());

        return modelAndView;
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thankYou(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());

        return "thanks";
    }


//    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
//    public ModelAndView createUser(@Valid User user, BindingResult result) {
//        ModelAndView modelAndView = new ModelAndView();
//        if (result.hasErrors()) {
//            modelAndView.setViewName("createUser");
//            modelAndView.addObject("user", user);
//            return modelAndView;
//        }
//        try {
//            userService.createUser(user);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
////        modelAndView.addObject("allUsers", userService.getAllUsers());
//        modelAndView.setViewName("userHome");
//        return modelAndView;
//    }


}
