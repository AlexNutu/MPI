package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.info.UserInformation;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/editUser", method = RequestMethod.POST,
            consumes = {"application/json"})
    public String editUser(@RequestBody UserInformation userInfo) throws NoSuchAlgorithmException {
        userService.editUser(userInfo);

        return "manageUser";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST,
            consumes = {"application/json"})
    public String createUser(@RequestBody UserInformation userInfo) throws NoSuchAlgorithmException {
        userService.createUser(userInfo);

        return "createUser";
    }


}
