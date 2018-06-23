package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.UserValidator;
import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.CategoryRepository;
import com.alenut.mpi.repository.FollowingRepository;
import com.alenut.mpi.repository.UserRepository;
import com.alenut.mpi.service.EmailServiceImpl;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.AutoLoginService;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.*;


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

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private FollowingRepository followingRepository;

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
                ideas = ideaService.getByTitleLikeOrBodyLike(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeas(page);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("currentCategory", category);
        model.addAttribute("categoryName", categoryName);

        User curentUser = getCurrentUser();
        if (curentUser != null) {
            return "redirect:/user/home";
        }
        return "userHome";
    }

    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.GET)
    public String viewIdea(HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {

        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);

        List<Idea> matchingIdeas = new ArrayList<>();
        List<Matching> matchings = idea.getMatchings();
        if (matchings.size() > 0) {
            for (Matching matching : matchings) {
                Idea idea1 = matching.getIdeaMatch();
                idea1.setSemantic(matching.getSemantic());
//                idea1.setSintactic(matching.getSintactic());
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
    public String userIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") Long userId, @RequestParam(defaultValue = "-1") long category) {

        User viewedUser = userRepository.getById(userId);
        model.addAttribute("viewedUser", viewedUser);
        Category choseCategory = new Category();

        Page<Idea> ideas = null;
        if (category != -1) {
            choseCategory = categoryRepository.getById(category);
            ideas = ideaService.getIdeasByUserAndCategory(page, viewedUser, choseCategory);
        } else {
            ideas = ideaService.getIdeasByUser(page, viewedUser);
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        List<Category> userCategoryList = categoryService.getUniqueCategoriesByUser(ideas);
        model.addAttribute("myCategoryList", userCategoryList);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        // construct the filteredCategoryList
        List<Category> filteredCategoryList = new ArrayList<>();
        if (category == -1) {
            filteredCategoryList = userCategoryList;
        } else {
            filteredCategoryList.add(choseCategory);
        }
        model.addAttribute("filteredCategoryList", filteredCategoryList);
        model.addAttribute("currentCategoryId", category);

        return "userIdeas";
    }

    @RequestMapping(value = "/viewProfile/{userId}", method = RequestMethod.GET)
    public String userProfile(HttpServletRequest request, @PathVariable Long userId, Model model) {
        User user = userService.getById(userId);

        List<Idea> ideaList = ideaService.getIdeasByUser(user);
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("occupation", user.getOccupation());
        model.addAttribute("phone", user.getPhone_number());
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
    public ModelAndView createUser(@Valid User user, RedirectAttributes redirectAttributes, BindingResult result, Model model, HttpServletRequest request) {
        model.addAttribute("hasError", "false");
        model.addAttribute("needConfirmation", "false");
        ModelAndView modelAndView = new ModelAndView("redirect:createUser");

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            model.addAttribute("hasError", "true");
            modelAndView.addObject("user", user);
//            modelAndView = new ModelAndView(new RedirectView("createUser"));
            modelAndView.setViewName("createUser");
            return modelAndView;
        }

        try {
            // generating the confirmation token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setReg_date(new Date());
            user.setRole(1);
            user.setConfirmed(0);
            user.setImage("user1.png");
            userService.createUser(user);

            // sending the confirmation e-mail
            try {
                emailService.sendSimpleMessage(
                        user.getEmail(),
                        "Confirm Account",
                        "Dear " + user.getFull_name() + ", \n\n You almost registered to My Project Idea! " + " \n\n " +
                                "Please confirm your e-mail by accessing the following link: " + "\n\n" +
                                " http://localhost:8090/home/confirm/" + token + "\n\n" +
                                " Best Regards, \n MPI Service");
            } catch (MailException mailException) {
                model.addAttribute("invalidEmail", "true");
            }
            if (!model.containsAttribute("invalidEmail")) {
                redirectAttributes.addFlashAttribute("needConfirmation", "true");
                redirectAttributes.addFlashAttribute("invalidEmail", "false");
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }


    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    public String confirmAccount(HttpServletRequest request, @PathVariable String token, Model model) {


        User createdUser = userService.getByToken(token);
        // daca este regasit un user cu acest token
        if (createdUser != null) {
            String notCrypedPass = createdUser.getPassword();
            if (createdUser.getConfirmed() == 0) {

                // actualizam datele utilizatorului
                createdUser.setPassword(BCrypt.hashpw(createdUser.getPassword(), BCrypt.gensalt()));
                createdUser.setConfirmed(1);
                userRepository.save(createdUser);

            } else {
                return "redirect:/user/home/?page=0";
            }

            autoLoginService.autoLogin(createdUser, notCrypedPass, request);

            return "redirect:/user/thanks";
        }

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/home/?page=0";
        } else {
            return "redirect:/user/home/?page=0";
        }
    }


    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public String displayPopularIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        String categoryName = "";
        Page<Idea> ideas = null;
        if (category != 0) { // daca este aleasa o categorie
            Category categoryChosed = categoryRepository.getById(category);
            categoryName = categoryChosed.getBody();
            ideas = ideaService.getByCategoryPopular(page, categoryChosed);

        } else { //daca s-a ales o categorie atunci filtrarea de search dispare
            if (!q.trim().toLowerCase().equals("")) {
                ideas = ideaService.getByTitleOrBodyLikePopular(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeasPopular(page);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);

        return "mostPopular";
    }

    @RequestMapping(value = "/multipleSimilarities", method = RequestMethod.GET)
    public String displayMultipleSimilaritiesIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        String categoryName = "";
        Page<Idea> ideas = null;
        if (category != 0) { // daca este aleasa o categorie
            Category categoryChosed = categoryRepository.getById(category);
            categoryName = categoryChosed.getBody();
            ideas = ideaService.getByCategorySimilarities(page, categoryChosed);

        } else { //daca s-a ales o categorie atunci filtrarea de search dispare
            if (!q.trim().toLowerCase().equals("")) {
                ideas = ideaService.getByTitleOrBodyLikeSimilarities(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeasSimilarities(page);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);

        return "multipleSimilarities";
    }

    @RequestMapping(value = "/mostCommented", method = RequestMethod.GET)
    public String displayMostCommentedIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        String categoryName = "";
        Page<Idea> ideas = null;
        if (category != 0) { // daca este aleasa o categorie
            Category categoryChosed = categoryRepository.getById(category);
            categoryName = categoryChosed.getBody();
            ideas = ideaService.getByCategoryComments(page, categoryChosed);

        } else { //daca s-a ales o categorie atunci filtrarea de search dispare
            if (!q.trim().toLowerCase().equals("")) {
                ideas = ideaService.getByTitlOrBodyeLikeComments(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeasComments(page);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);

        return "mostCommented";
    }

    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public String categoryChart(HttpServletRequest request, Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o2.getIdeasFromCategory().size() - o1.getIdeasFromCategory().size();
            }
        });

        model.addAttribute("category1", categoryList.get(0).getBody());
        model.addAttribute("category2", categoryList.get(1).getBody());
        model.addAttribute("category3", categoryList.get(2).getBody());
        model.addAttribute("category4", categoryList.get(3).getBody());
        model.addAttribute("category5", categoryList.get(4).getBody());
        model.addAttribute("nrIdeas1", categoryList.get(0).getIdeasFromCategory().size());
        model.addAttribute("nrIdeas2", categoryList.get(1).getIdeasFromCategory().size());
        model.addAttribute("nrIdeas3", categoryList.get(2).getIdeasFromCategory().size());
        model.addAttribute("nrIdeas4", categoryList.get(3).getIdeasFromCategory().size());
        model.addAttribute("nrIdeas5", categoryList.get(4).getIdeasFromCategory().size());

        return "categoryChart";
    }


    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        return "contact";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "about";
    }

}
