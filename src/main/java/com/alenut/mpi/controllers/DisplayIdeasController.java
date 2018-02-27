package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.service.impl.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class DisplayIdeasController extends BaseController {

    @Autowired
    private IdeaService ideaService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getIdeas(HttpServletRequest request, Model model) {
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
    public String publishIdea(@RequestBody Idea idea) {
        idea.setCreator(getCurrentUser().getId());
        ideaService.insert(idea);
        return "Idea was pusblished";
    }

    @GetMapping(value = "/viewIdea")
    public String viewIdea(HttpServletRequest request, Model model){//(@RequestParam Idea idea) {
        //TODO: Extragere informatii despre ideea curenta, parametrul primit cat si tipul de request trebuie revizuite
        return "idea";
    }

}
