package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.MD5Encryption;
import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.FollowingRepository;
import com.alenut.mpi.service.EmailServiceImpl;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import com.alenut.mpi.service.impl.PictureLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    EmailServiceImpl emailService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/user/home/?page=0";
    }


    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public ModelAndView createUserView(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        ModelAndView modelAndView = new ModelAndView("createUser");
        modelAndView.addObject("user", new User());

        return modelAndView;
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thankYou(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "thanks";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String myProfile(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        List<Idea> ideaList = ideaService.getIdeasByUser(user);
        model.addAttribute("phone", user.getPhone_number());
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("occupation", user.getOccupation());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("image", user.getImage());

        model.addAttribute("noOfIdeas", userService.getNoOfIdeas(ideaList));
        model.addAttribute("noOfMatchings", userService.getNoOfMatchings(ideaList));
        model.addAttribute("noOfLikes", userService.getNoOfLikes(ideaList));
        model.addAttribute("noOfComments", userService.getNoOfComments(ideaList));

        return "myProfile";
    }

    @RequestMapping(value = "/viewProfile/{userId}", method = RequestMethod.GET)
    public String userProfile(HttpServletRequest request, @PathVariable Long userId, Model model) {
        User currentUser = getCurrentUser();
        User viewedUser = userService.getById(userId);

        List<Idea> ideaList = ideaService.getIdeasByUser(viewedUser);
        model.addAttribute("fullname", viewedUser.getFull_name());
        model.addAttribute("username", viewedUser.getUsername());
        model.addAttribute("occupation", viewedUser.getOccupation());
        model.addAttribute("phone", viewedUser.getPhone_number());
        List<Idea> myIdeas = ideaService.getIdeasByUser(currentUser);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("image", viewedUser.getImage());

        model.addAttribute("viewedUser", viewedUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("noOfIdeas", userService.getNoOfIdeas(ideaList));
        model.addAttribute("noOfMatchings", userService.getNoOfMatchings(ideaList));
        model.addAttribute("noOfLikes", userService.getNoOfLikes(ideaList));
        model.addAttribute("noOfComments", userService.getNoOfComments(ideaList));
        List<Following> followings = followingRepository.getByUser(currentUser);
        boolean ok = false;
        for (Following following : followings) {
            if (following.getFollowingUser().equals(viewedUser)) {
                ok = true;
                break;
            }
        }
        if (ok) {
            model.addAttribute("following", true);
        } else {
            model.addAttribute("following", false);
        }

        return "viewProfile";
    }

    @PostMapping("/follow")
    public String followUser(@RequestParam(defaultValue = "0") long idUser) {
        User currentUser = getCurrentUser();
        User followingUser = userService.getById(idUser);

        List<Following> followings = followingRepository.getByUser(currentUser);
        boolean ok = false;
        Following foundFollowing = null;
        for (Following following : followings) {
            if (following.getFollowingUser().equals(followingUser)) {
                ok = true;
                foundFollowing = following;
                break;
            }
        }
        if (ok) {
            followingRepository.delete(foundFollowing);
        } else {
            Following following = new Following();
            following.setFollowingUser(followingUser);
            following.setUser(currentUser);
            followingRepository.save(following);
        }

        return "redirect:/user/viewProfile/" + idUser;
    }

    @RequestMapping(value = "/accountSettings", method = RequestMethod.GET)
    public ModelAndView accountSettings(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(currentUser).size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("image", currentUser.getImage());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
