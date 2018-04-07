package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.MD5Encryption;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import com.alenut.mpi.service.impl.PictureLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    IdeaServiceImpl ideaService;

    @Autowired
    PictureLoaderService pictureLoaderService;

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

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        List<Idea> ideaList = ideaService.getIdeasByUser(user);
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("occupation", user.getOccupation());
        model.addAttribute("ideasNumber", ideaList.size());
        model.addAttribute("image", user.getImage());

        model.addAttribute("noOfIdeas", userService.getNoOfIdeas(ideaList));
        model.addAttribute("noOfMatchings", userService.getNoOfMatchings(ideaList));
        model.addAttribute("noOfLikes", userService.getNoOfLikes(ideaList));
        model.addAttribute("noOfComments", userService.getNoOfComments(ideaList));

        return "myProfile";
    }

    @RequestMapping(value = "/accountSettings", method = RequestMethod.GET)
    public ModelAndView accountSettings(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(currentUser).size());
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("image", currentUser.getImage());

        ModelAndView modelAndView = new ModelAndView("accountSettings");
        modelAndView.addObject("user", currentUser);

        if (!model.containsAttribute("displaySuccess")) {
            model.addAttribute("displaySuccess", "false");
        }
        if (!model.containsAttribute("displayError")) {
            model.addAttribute("displayError", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/accountSettings", method = RequestMethod.POST)
    public ModelAndView accountSettings(@Valid User user, BindingResult result, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image, HttpServletRequest request) throws NoSuchAlgorithmException, IOException {

        User currentUser = getCurrentUser();
        ModelAndView modelAndView = new ModelAndView("redirect:accountSettings");
        modelAndView.addObject("user", user);

        Boolean parola = false, error = false;
        // validate the old password
        if (!user.getPassword().equals("")) {
            if (!user.getNewPassword().equals("")) {
                if (currentUser.getPassword().equals(MD5Encryption.computeMD5(user.getPassword()))) {
                    user.setPassword(MD5Encryption.computeMD5(user.getNewPassword()));

                    if (!user.getImage().equals(currentUser.getImage())) { // daca a fost schimbata imaginea atunci o adaugam in proiect
//                        pictureLoaderService.deletePictureFromDisk(user.getImage());
                        pictureLoaderService.deletePictureFromDisk(currentUser.getImage());
                        String imagePath = userService.saveImage(image, user);
                        user.setImage(imagePath);
                    }
                    redir.addFlashAttribute("displaySuccess", "true");
                    userService.editUser(user, currentUser.getId());
                    parola = true;
                } else {
                    redir.addFlashAttribute("displayError", "true");
                    error = true;
                }
            }
        }
        if (!parola && !error) {
            if (!user.getImage().equals(currentUser.getImage())) { // daca a fost schimbata imaginea atunci o adaugam in proiect
                pictureLoaderService.deletePictureFromDisk(currentUser.getImage());
//                pictureLoaderService.deletePictureFromDisk(user.getImage());
                String imagePath = userService.saveImage(image, user);
                user.setImage(imagePath);
            }
            redir.addFlashAttribute("displaySuccess", "true");
            user.setPassword(currentUser.getPassword());
            userService.editUser(user, currentUser.getId());
        }

        return modelAndView;
    }


}
