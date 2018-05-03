package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.IdeaValidator;
import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.CategoryRepository;
import com.alenut.mpi.repository.ConversationRepository;
import com.alenut.mpi.repository.IdeaRepository;
import com.alenut.mpi.repository.UserRepository;
import com.alenut.mpi.service.UserService;
import com.alenut.mpi.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private AppreciationService appreciationService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String displayAllIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentUser", user);
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

        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);
        model.addAttribute("messagesNumber", user.getMessages().size());

        return "userHome";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public User findOne(Long id) {
        User user2 = userRepository.findOne(id);
        User user = new User();
        user.setId(user2.getId());
        user.setFull_name(user2.getFull_name());
        return user;
    }

    @GetMapping("/findOneIdea")
    @ResponseBody
    public Idea findOneIdea(Long id) {
        Idea idea2 = ideaRepository.findOne(id);
        Idea idea = new Idea();
        idea.setId(idea2.getId());
        return idea;
    }

    @PostMapping("/send")
    public String sendMessage(Message m, @RequestParam(defaultValue = "0") int page) {

        // check if conversation exists
        User currentUser = getCurrentUser();
        Long idReceiver = m.getId_receiver();
        List<Conversation> conversations = conversationService.getAllUserConversations(currentUser); // get all conversations from the current user
        boolean exists = false;
        Conversation findConversation = null;
        for (Conversation conversation : conversations) {
            if ((conversation.getUser().getId().equals(currentUser.getId()) && conversation.getUser2().getId().equals(idReceiver)) ||
                    (conversation.getUser().getId().equals(idReceiver) && conversation.getUser2().getId().equals(currentUser.getId()))) {
                exists = true;
                findConversation = conversation;
            }
        }
        if (!exists) {
            Conversation newConversation = new Conversation();
            newConversation.setCreatedDate(new Date());
            newConversation.setUser(currentUser);
            newConversation.setUser2(userService.getById(idReceiver));
            // adding the new conversation
            conversationRepository.save(newConversation);

            // if the new conversation was created
            // retrieve the updated list of conversations
            conversations = conversationService.getAllUserConversations(currentUser);
            //setting the conversation's last message and the message's conversation
            for (Conversation conversation : conversations) {
                if ((conversation.getUser().getId().equals(currentUser.getId()) && conversation.getUser2().getId().equals(idReceiver)) ||
                        (conversation.getUser().getId().equals(idReceiver) && conversation.getUser2().getId().equals(currentUser.getId()))) {
                    findConversation = conversation;
                }
            }
        }

        m.setConversation(findConversation);
        m.setSender(currentUser);
        m.setId_receiver(idReceiver);
        m.setSend_date(new Date().toString());
        messageService.addMessage(m);

        return "redirect:/user/home/?page=" + page;
    }

    @PostMapping("/deleteIdea")
    public String deleteIdea(Idea ideaDelete, @RequestParam(defaultValue = "0") int page) {
        // delete all info that corresponds to this idea
        //TODO: pentru performanta sa dau ca parametru pentru metoda, ideea din antet
        Idea idea = ideaRepository.getById(ideaDelete.getId());

        //delete matchings (verify if it idea or the matching idea)
        matchService.deleteMatchingsByIdea(idea);
        //delete comments
        commentsService.deleteCommentsByIdea(idea);
        //delete appreciations
        appreciationService.deleteAppreciationsByIdea(idea);
        //delete tags
        tagService.deleteTagsByIdea(idea);

        ideaService.deleteIdea(idea);
        return "redirect:/user/home/?page=" + page;
    }

    @RequestMapping(value = "/myIdeas", method = RequestMethod.GET)
    public String myIdeas(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("occupation", user.getOccupation());

        List<Idea> ideas = ideaService.getIdeasByUser(user);
        model.addAttribute("ideasList", ideas);
        model.addAttribute("myIdeasNumber", ideas.size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        List<Category> categories = categoryService.getUniqueCategoriesByUser(ideas);
        model.addAttribute("categoryList", categories);

        return "myIdeas";
    }

    @RequestMapping(value = "/postIdea", method = RequestMethod.GET)
    public ModelAndView postIdeaView(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
        model.addAttribute("currentUser", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
        if (convOpen.getUser().equals(user)) {
            sentMessage.setId_receiver(convOpen.getUser2().getId());
        } else {
            sentMessage.setId_receiver(user.getId());
        }
        messageService.addMessage(sentMessage);

        return "redirect:{conversationId}";
    }


    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "about";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactUs(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        return "contact";
    }

}
