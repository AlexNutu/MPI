package com.alenut.mpi.controllers;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.service.impl.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminHomeController extends BaseController {

    @Autowired
    private IdeaService ideaService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String displayAllRequestsFromAllUsersExceptMine(HttpSession session, Model model) {
        int[] days;
        if (getCurrentUser().getId() == 1) {
            return "redirect:/login";
        }

//        Long adminId = getCurrentUser().getId();
//        List<Request> allRequestsExceptMine = requestService.getAllRequestsExceptMine(adminId);
//        days = userDataServiceImpl.getDates(adminId, userService, holidayService, requestService);
//        int additionalVacation = userService.getById(adminId).getAdditionalVacation();
//        model.addAttribute("remainingDays", days[0] + additionalVacation);
//        model.addAttribute("remainingDaysToDate", days[2] + additionalVacation);
//        model.addAttribute("requestedDays", days[1]);
//        model.addAttribute("medicalDays", days[4]);
//        model.addAttribute("approvedDays", userDataServiceImpl.getApprovedDaysThisYear(holidayService.getAll(), requestRepository.getVacationRequestOnly(adminId)));
//        model.addAttribute("requests", allRequestsExceptMine);
//
//        Integer totalQuery = (Integer) session.getAttribute("totalQuery");
//        model.addAttribute("totalQuery", totalQuery);
//        session.setAttribute("totalQuery", null);

        return "adminHome";
    }

    @PostMapping(value = "/postIdea")
    public String publishIdea(@RequestBody Idea idea) {
        idea.setCreator(getCurrentUser().getId());
        ideaService.insert(idea);
        return "Idea was pusblished";
    }
}
