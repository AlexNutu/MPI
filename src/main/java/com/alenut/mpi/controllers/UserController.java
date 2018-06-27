package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Following;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.FollowingRepository;
import com.alenut.mpi.repository.UserRepository;
import com.alenut.mpi.service.EmailServiceImpl;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.AutoLoginService;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import com.alenut.mpi.service.impl.PictureLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private AutoLoginService autoLoginService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/user/home/?page=0";
    }

    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thankYou(HttpServletRequest request, Model model) {

        User currentUser = getCurrentUser();
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(currentUser).size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        List<Following> followingList = followingRepository.getByUser(currentUser);
        List<User> followingUsers = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (followingUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
                followingUsers.add(following.getFollowingUser());
            } else {
                followingUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);

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
        List<Following> followingList = followingRepository.getByUser(user);
        List<User> followingUsers = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (followingUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
                followingUsers.add(following.getFollowingUser());
            } else {
                followingUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);

        model.addAttribute("noOfIdeas", userService.getNoOfIdeas(ideaList));
        model.addAttribute("noOfMatchings", userService.getNoOfMatchings(ideaList));
        model.addAttribute("noOfLikes", userService.getNoOfLikes(ideaList));
        model.addAttribute("noOfComments", userService.getNoOfComments(ideaList));

        // verify if the current user has column  alert = 0 or 1
        if (user.getAlert() == 1) {
            model.addAttribute("alerting", true);
        } else {
            model.addAttribute("alerting", false);
        }

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
        List<Following> followingList = followingRepository.getByUser(currentUser);
        List<User> followingUsers = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (followingUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
                followingUsers.add(following.getFollowingUser());
            } else {
                followingUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);
        if (displayedUsers.contains(viewedUser)) {
            displayedUsers.remove(viewedUser);
            displayedUsers.add(viewedUser);
        }

        model.addAttribute("viewedUser", viewedUser);
        model.addAttribute("currentUser", currentUser);
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
            following.setDate_following(new Date().toString());
            followingRepository.save(following);
        }

        return "redirect:/user/viewProfile/" + idUser;
    }

    @PostMapping("/alert")
    public String alertMe(@RequestParam(defaultValue = "0") long idUser) {

        User currentUser = getCurrentUser();
        if (currentUser.getAlert() == 1) {
            userService.updateAlertForUser(0, currentUser);
        } else {
            userService.updateAlertForUser(1, currentUser);
        }

        return "redirect:/user/profile";
    }

    @RequestMapping(value = "/accountSettings", method = RequestMethod.GET)
    public ModelAndView accountSettings(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(currentUser).size());
        if (currentUser.getMessages() != null) {
            model.addAttribute("messagesNumber", currentUser.getMessages().size());
        } else {
            model.addAttribute("messagesNumber", 0);
        }
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("image", currentUser.getImage());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        List<Following> followingList = followingRepository.getByUser(currentUser);
        List<User> followingUsers = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (followingUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
                followingUsers.add(following.getFollowingUser());
            } else {
                followingUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);

        ModelAndView modelAndView = new ModelAndView("accountSettings");
        modelAndView.addObject("user", currentUser);

        if (!model.containsAttribute("displaySuccess")) {
            model.addAttribute("displaySuccess", "false");
        }
        if (!model.containsAttribute("displayError")) {
            model.addAttribute("displayError", "false");
        }
        if (!model.containsAttribute("largeImage")) {
            model.addAttribute("largeImage", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/accountSettings", method = RequestMethod.POST)
    public ModelAndView accountSettings(@Valid User user, BindingResult result, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image, HttpServletRequest request) throws NoSuchAlgorithmException, IOException {

        User currentUser = getCurrentUser();
        ModelAndView modelAndView = new ModelAndView("redirect:accountSettings");
        modelAndView.addObject("user", user);

        if (!currentUser.getImage().equals(user.getImage()) || !currentUser.getFull_name().equals(user.getFull_name()) ||
                !currentUser.getUsername().equals(user.getUsername()) || !currentUser.getPhone_number().equals(user.getPhone_number())
                || !currentUser.getOccupation().equals(user.getOccupation()) || !user.getPassword().equals("")) {

            user.setFull_name(user.getFull_name().trim().replaceAll(" +", " "));
            user.setUsername(user.getUsername().trim().replaceAll(" +", " "));

            Boolean parola = false, error = false;
            // validate the old password
            if (!user.getPassword().equals("")) {
                if (!user.getNewPassword().equals("")) {

                    if (BCrypt.checkpw(user.getPassword(), currentUser.getPassword())) {

                        user.setPassword(BCrypt.hashpw(user.getNewPassword(), BCrypt.gensalt()));
                        Boolean imageSuccess = true;
                        if (!user.getImage().equals(currentUser.getImage())) { // daca a fost schimbata imaginea atunci o adaugam in proiect
                            if (image.getSize() <= 3000000) {
                                if (!currentUser.getImage().contains("user1.png")) {
                                    pictureLoaderService.deletePictureFromDisk(currentUser.getImage());
                                }
                                String imagePath = userService.saveImage(image, user);
                                user.setImage(imagePath);
                            } else {
                                imageSuccess = false;
                                redir.addFlashAttribute("largeImage", "true");
                            }
                        }
                        if (imageSuccess) {
                            redir.addFlashAttribute("displaySuccess", "true");
                            userService.editUser(user, currentUser.getId());
                        }
                        parola = true;
                    } else {
                        redir.addFlashAttribute("displayError", "true");
                        error = true;
                    }
                }
            }
            if (!parola && !error) {
                Boolean imageSuccess = true;
                if (!user.getImage().equals(currentUser.getImage())) { // daca a fost schimbata imaginea atunci o adaugam in proiect
                    if (image.getSize() <= 3000000) {
                        if (!currentUser.getImage().contains("user1.png")) {
                            pictureLoaderService.deletePictureFromDisk(currentUser.getImage());
                        }
                        String imagePath = userService.saveImage(image, user);
                        user.setImage(imagePath);
                    } else {
                        imageSuccess = false;
                        redir.addFlashAttribute("largeImage", "true");
                    }
                }
                if (imageSuccess) {
                    redir.addFlashAttribute("displaySuccess", "true");
                    user.setPassword(currentUser.getPassword());
                    userService.editUser(user, currentUser.getId());
                }
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "/editUser/{userId}", method = RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request, @PathVariable Long userId, Model model) {

        User currentUser = getCurrentUser();
        User editUser = userService.getById(userId);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(currentUser).size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());

        model.addAttribute("username", editUser.getUsername());
        model.addAttribute("image", editUser.getImage());
        model.addAttribute("userId", editUser.getId());
        model.addAttribute("password", editUser.getPassword());

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        List<Following> followingList = followingRepository.getByUser(currentUser);
        List<User> followingUsers = new ArrayList<>();
        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (followingUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
                followingUsers.add(following.getFollowingUser());
            } else {
                followingUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);

        ModelAndView modelAndView = new ModelAndView("editUser");
        modelAndView.addObject("user", editUser);

        if (!model.containsAttribute("displaySuccess")) {
            model.addAttribute("displaySuccess", "false");
        }
        if (!model.containsAttribute("displayError")) {
            model.addAttribute("displayError", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/editUser/{userId}", method = RequestMethod.POST)
    public ModelAndView editUser(@Valid User user, @PathVariable Long userId, BindingResult result, Model model, RedirectAttributes redir, HttpServletRequest request) throws NoSuchAlgorithmException, IOException {
        User editUser = userService.getById(userId);
        ModelAndView modelAndView = new ModelAndView("redirect:{userId}");

        if (!editUser.getFull_name().equals(user.getFull_name()) || !editUser.getUsername().equals(user.getUsername())
                || !editUser.getPhone_number().equals(user.getPhone_number()) || !editUser.getOccupation().equals(user.getOccupation())) {

            user.setFull_name(user.getFull_name().trim().replaceAll(" +", " "));
            user.setUsername(user.getUsername().trim().replaceAll(" +", " "));
            user.setPassword(editUser.getPassword());
            modelAndView.addObject("user", user);

            redir.addFlashAttribute("displaySuccess", "true");
            user.setPassword(editUser.getPassword());
            userService.editUser2(user, userId);
        }

        return modelAndView;
    }

}
