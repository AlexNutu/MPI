package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.IdeaValidator;
import com.alenut.mpi.entities.*;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.CategoryServiceImpl;
import com.alenut.mpi.service.impl.ConversationService;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import com.alenut.mpi.service.impl.MessageService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.ss.formula.functions.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
import java.util.*;

@Controller
@RequestMapping("/user")
public class HomeController extends BaseController {

    @Autowired
    private IdeaServiceImpl ideaService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private IdeaValidator ideaValidator;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String displayAllIdeas(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
//        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        List<Idea> ideas = ideaService.getAllIdeas();
        model.addAttribute("ideasList", ideas);

//        retrieve the last conversation, if it not affects performance
//        List<Conversation> conversations = conversationService.getAllUserConversations(user);
//        model.addAttribute("messagesNumber", messageService.getMessagesByUser(user).size());

        return "userHome";
    }

    @RequestMapping(value = "/myIdeas", method = RequestMethod.GET)
    public String myIdeas(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("occupation", user.getOccupation());

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
        if (!model.containsAttribute("duplicate")) {
            model.addAttribute("duplicate", "false");
        }
        if (!model.containsAttribute("format")) {
            model.addAttribute("format", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/postIdea", method = RequestMethod.POST)
    public ModelAndView postIdea(@Valid Idea idea, BindingResult result, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image) throws Exception {
        model.addAttribute("categories", categoryService.getAllCategories());

        User user = getCurrentUser();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("idea", idea);

        idea.setUser(user);
        ideaValidator.validate(idea, result);

        if (result.hasErrors()) {
            String error = result.getAllErrors().get(0).getCode();
            if (error.equals("duplicate")) {
                model.addAttribute("duplicate", "true");
            } else {
                model.addAttribute("format", "true");
            }
            modelAndView.setViewName("postIdea");
        } else {
            modelAndView.setViewName("redirect:postIdea");
            // image uploading on file disk
            if (!idea.getImage_path().equals("")) {
                String imagePath = ideaService.saveIdeaImage(image, idea);
                idea.setImage_path(imagePath);
            } else {
                idea.setImage_path("idea7.jpg");
            }
            ideaService.insert(idea, user);
            // Add matchings with this idea
            ideaService.addMatchings(idea);
            // Add tags/keywords for this idea
            ideaService.addTags(idea);

            redir.addFlashAttribute("displaySuccess", "true");
            redir.addFlashAttribute("idCreatedIdea", idea.getId());
        }

        return modelAndView;
    }

    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.GET)
    public String viewIdea(HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {//(@RequestParam Idea idea) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        model.addAttribute("comment", new Comment());

        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);

        List<Idea> matchingIdeas = new ArrayList<>();
        List<Matching> matchings = idea.getMatchings();
        if (matchings.size() > 0) {
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

    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.POST)
    public String viewIdeaPostComment(@Valid Comment comment, BindingResult result, HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {
        User currentUser = getCurrentUser();
        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);

        comment.setPosted_date(new Date().toString());
        comment.setUser(currentUser);
        comment.setIdea(idea);
        ideaService.addComment(comment);

        return "redirect:{ideaId}";
    }

    @RequestMapping(value = "/messages/{conversationId}", method = RequestMethod.GET)
    public String myMessagesGet(HttpServletRequest httpServletRequest, @PathVariable Long conversationId, Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());

        model.addAttribute("sentMessage", new Message());

        List<Conversation> conversations = conversationService.getAllUserConversations(user);
        Collections.sort(conversations, (c1, c2) -> {
            if (c1.getCreatedDate().equals(c2.getCreatedDate()))
                return 0;
            if (c1.getCreatedDate().before(c2.getCreatedDate()))
                return -1;
            return 1;
        });
        model.addAttribute("conversations", conversations);

        if (conversationId == -1) {
            conversationId = conversations.get(0).getId();
        }
        model.addAttribute("convOpen", conversationService.getById(conversationId));

        return "messages";
    }

    @RequestMapping(value = "/messages/{conversationId}", method = RequestMethod.POST)
    public String myMessagesPost(@Valid Message sentMessage, BindingResult result, @PathVariable Long conversationId, HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());
        List<Conversation> conversations = conversationService.getAllUserConversations(user);
        model.addAttribute("conversations", conversations);

        Conversation convOpen = conversationService.getById(conversationId);
        model.addAttribute("convOpen", convOpen);

        sentMessage.setSend_date(new Date().toString());
        sentMessage.setSender(user);
        sentMessage.setConversation(convOpen);
        messageService.addMessage(sentMessage);

        return "redirect:{conversationId}";
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

}
