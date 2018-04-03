package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.IdeaValidator;
import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class HomeController extends BaseController {

    @Autowired
    private IdeaServiceImpl ideaService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private IdeaValidator ideaValidator;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String displayAllIdeas(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        List<Idea> ideas = ideaService.getAllIdeas();
        model.addAttribute("ideasList", ideas);

        return "userHome";
    }

    @RequestMapping(value = "/myIdeas", method = RequestMethod.GET)
    public String myIdeas(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());

        List<Idea> ideas = ideaService.getIdeasByUser(user);
        model.addAttribute("ideasList", ideas);
        model.addAttribute("ideasNumber", ideas.size());
        List<Category> categories = categoryService.getUniqueCategoriesByUser(ideas);
        model.addAttribute("categoryList", categories);

        return "myIdeas";
    }

    @RequestMapping(value = "/postIdea", method = RequestMethod.GET)
    public ModelAndView postIdeaView(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("categories", categoryService.getAllCategories());

        ModelAndView modelAndView = new ModelAndView("postIdea");
        modelAndView.addObject("idea", new Idea());

        if (!model.containsAttribute("displaySuccess")) {
            model.addAttribute("displaySuccess", "false");
        }
        if (!model.containsAttribute("displayError")) {
            model.addAttribute("displayError", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/postIdea", method = RequestMethod.POST)
    public ModelAndView postIdea(@Valid Idea idea, BindingResult result, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image) throws IOException {
        User user = getCurrentUser();
        List<Idea> ideas = ideaService.getIdeasByUser(user);
        model.addAttribute("ideasList", ideas);

        ModelAndView modelAndView = new ModelAndView("redirect:postIdea");
        modelAndView.addObject("idea", idea);

        idea.setUser(user);
        ideaValidator.validate(idea, result);

        if (result.hasErrors()) {
            redir.addFlashAttribute("displayError", "true");
        } else {
            // image uploading on file disk
            if (idea.getImage_path() != null) {
                String imagePath = ideaService.saveIdeaImage(image, user);
                idea.setImage_path(imagePath);
            }
            redir.addFlashAttribute("displaySuccess", "true");
            ideaService.insert(idea, user);
        }

        return modelAndView;
    }

    @GetMapping(value = "/viewIdea")
    public String viewIdea(HttpServletRequest request, Model model) {//(@RequestParam Idea idea) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        //TODO: Extragere informatii despre ideea curenta, parametrul primit cat si tipul de request trebuie revizuite
        return "viewIdea";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String myMessages(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        return "messages";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        return "about";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        return "contact";
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
