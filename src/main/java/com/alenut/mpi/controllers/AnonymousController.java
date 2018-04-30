package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.UserValidator;
import com.alenut.mpi.entities.Comment;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.AutoLoginService;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAllIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page) {

        Page<Idea> ideas = ideaService.getAllIdeas(page);
        model.addAttribute("ideasList", ideas.getContent());

        return "userHome";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public ModelAndView createUserView(HttpServletRequest request, Model model) {

        ModelAndView modelAndView = new ModelAndView("createUser");
        modelAndView.addObject("user", new User());

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



    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        return "contact";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        return "about";
    }


    //    public String populateTable(Model model) {
//        int[] days;
//        Long id;
//
//        id = getCurrentUser().getId();
//        days = userDataServiceImpl.getDates(id, userService, holidayService, requestService);
//        int additionalVacation = userService.getById(id).getAdditionalVacation();
//        model.addAttribute("remainingDays", days[0] + additionalVacation);
//        model.addAttribute("remainingDaysToDate", days[2] + additionalVacation);
//        model.addAttribute("requestedDays", days[1]);
//        model.addAttribute("approvedDays", userDataServiceImpl.getApprovedDaysThisYear(holidayService.getAll(), requestRepository.getVacationRequestOnly(id)));
//        model.addAttribute("medicalDays", days[4]);
//        return "userHome";
//    }

}
