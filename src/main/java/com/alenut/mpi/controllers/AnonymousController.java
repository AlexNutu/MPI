package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.UserValidator;
import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.CategoryRepository;
import com.alenut.mpi.repository.UserRepository;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.AutoLoginService;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/home")
public class AnonymousController extends BaseController {

    @Autowired
    private IdeaServiceImpl ideaService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private AutoLoginService autoLoginService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAllIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        String categoryName = "";
        Page<Idea> ideas = null;
        if (category != 0) { // daca este aleasa o categorie
            Category categoryChose = categoryRepository.getById(category);
            ideas = ideaService.getByCategory(page, categoryChose);
            categoryName = categoryChose.getBody();
        } else { //daca s-a ales o categorie atunci filtrarea de search dispare
            if (!q.trim().toLowerCase().equals("")) {
                ideas = ideaService.getByTitleLike(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeas(page);
            }
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("currentCategory", category);
        model.addAttribute("categoryName", categoryName);

        return "userHome";
    }

    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.GET)
    public String viewIdea(HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {

        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);

        List<Idea> matchingIdeas = new ArrayList<>();
        List<Matching> matchings = idea.getMatchings();
        if(matchings.size() > 0){
            for (Matching matching : matchings) {
                Idea idea1 = matching.getIdeaMatch();
                idea1.setSemantic(matching.getSemantic());
                idea1.setSintactic(matching.getSintactic());
                if (idea1.getBody().length() > 209) {
                    idea1.setBody(idea1.getBody().substring(0, 208) + " ...");
                }
                matchingIdeas.add(idea1);
            }
        }

        model.addAttribute(matchingIdeas);

        return "viewIdea";
    }

    @RequestMapping(value = "/userIdeas", method = RequestMethod.GET)
    public String userIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") Long userId) {

        User viewedUser = userRepository.getById(userId);
        model.addAttribute("viewedUser", viewedUser);

        Page<Idea> ideas = null;
        ideas = ideaService.getIdeasByUser(page, viewedUser);

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        List<Category> myCategoryList = categoryService.getUniqueCategoriesByUser(ideas);
        model.addAttribute("myCategoryList", myCategoryList);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "userIdeas";
    }

    @RequestMapping(value = "/viewProfile/{userId}", method = RequestMethod.GET)
    public String userProfile(HttpServletRequest request, @PathVariable Long userId, Model model) {
        User user = userService.getById(userId);

        List<Idea> ideaList = ideaService.getIdeasByUser(user);
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("occupation", user.getOccupation());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("image", user.getImage());


        model.addAttribute("viewedUser", user);
        model.addAttribute("noOfIdeas", userService.getNoOfIdeas(ideaList));
        model.addAttribute("noOfMatchings", userService.getNoOfMatchings(ideaList));
        model.addAttribute("noOfLikes", userService.getNoOfLikes(ideaList));
        model.addAttribute("noOfComments", userService.getNoOfComments(ideaList));

        return "viewProfile";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public ModelAndView createUserView(HttpServletRequest request, Model model) {

        ModelAndView modelAndView = new ModelAndView("createUser");
        modelAndView.addObject("user", new User());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return modelAndView;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        String initialPassword = "";
        model.addAttribute("hasError", "false");
        ModelAndView modelAndView = new ModelAndView(new RedirectView("/user/thanks"));

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            model.addAttribute("hasError", "true");
            modelAndView.setViewName("createUser");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        try {
            user.setReg_date(new Date());
            user.setRole(1);
            user.setImage("user1.png");
            initialPassword = user.getPassword(); // deoarece parola se schimba la salvarea in baza de date
            userService.createUser(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        autoLoginService.autoLogin(user, initialPassword, request);

        model.addAttribute("username", user.getUsername());
        return modelAndView;
    }


    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        return "contact";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        return "about";
    }

}
