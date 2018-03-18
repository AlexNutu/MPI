package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.IdeaService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class HomeController extends BaseController {

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getIdeas(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "userHome";
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

    @PostMapping(value = "/postIdea")
    public String publishIdea(@RequestBody Idea idea, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        idea.setCreator(getCurrentUser().getId());
        ideaService.insert(idea);
        return "Idea was pusblished";
    }

    @GetMapping(value = "/viewIdea")
    public String viewIdea(HttpServletRequest request, Model model) {//(@RequestParam Idea idea) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        //TODO: Extragere informatii despre ideea curenta, parametrul primit cat si tipul de request trebuie revizuite
        return "idea";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "userProfile";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "contact";
    }

    @RequestMapping(value = "/myIdeas", method = RequestMethod.GET)
    public String myIdeas(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "myIdeas";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String myMessages(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "messages";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("username", user.getUsername());

        return "about";
    }

}
