package com.alenut.mpi.controllers;

import com.alenut.mpi.auxiliary.IdeaValidator;
import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.*;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private FollowingRepository followingRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AppreciationRepository appreciationRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PictureLoaderService pictureLoaderService;

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
                ideas = ideaService.getByTitleLikeOrBodyLike(page, "%" + q + "%");
            } else {
                ideas = ideaService.getAllIdeas(page);
            }
        }

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

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
        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);
        model.addAttribute("messagesNumber", user.getMessages().size());
        if (!model.containsAttribute("deleted")) {
            model.addAttribute("deleted", false);
        }

        return "userHome";
    }

    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public String displayPopularIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentUser", user);
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

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

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

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);
        model.addAttribute("messagesNumber", user.getMessages().size());

        return "mostPopular";
    }

    @RequestMapping(value = "/multipleSimilarities", method = RequestMethod.GET)
    public String displayMultipleSimilaritiesIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentUser", user);
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

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

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

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);
        model.addAttribute("messagesNumber", user.getMessages().size());

        return "multipleSimilarities";
    }

    @RequestMapping(value = "/mostCommented", method = RequestMethod.GET)
    public String displayMostCommentedIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") long category) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentUser", user);
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

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

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

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("qTitle", q);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentCategory", category);
        model.addAttribute("messagesNumber", user.getMessages().size());

        return "mostCommented";
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

    @PostMapping("/likedislike")
    public String giveLikeDislike(@RequestParam(defaultValue = "0") long idIdea) {
        User currentUser = getCurrentUser();
        Idea currentIdea = ideaRepository.getById(idIdea);
        List<Appreciation> appreciations = appreciationRepository.getByIdea(currentIdea);
        Appreciation foundAppreciation = null;
        for (Appreciation appreciation : appreciations) {
            if (appreciation.getUser().equals(currentUser)) {
                foundAppreciation = appreciation;
                break;
            }
        }
        if (foundAppreciation != null) {
            appreciationRepository.delete(foundAppreciation);
            ideaService.updateLikesMinus(currentIdea);
        } else {
            Appreciation appreciation = new Appreciation();
            appreciation.setIdea(currentIdea);
            appreciation.setUser(currentUser);
            appreciationRepository.save(appreciation);
            ideaService.updateLikesPlus(currentIdea);
        }

        return "redirect:/user/home";
    }

    @GetMapping("/findOneIdea")
    @ResponseBody
    public Idea findOneIdea(Long id) {
        Idea idea2 = ideaRepository.findOne(id);
        Idea idea = new Idea();
        idea.setId(idea2.getId());
        return idea;
    }

    @GetMapping("/findOneComment")
    @ResponseBody
    public Comment findOneComment(Long id) {
        Comment comment2 = commentRepository.findOne(id);
        Comment comment = new Comment();
        comment.setId(comment2.getId());
        return comment;
    }

    @PostMapping("/send")
    public String sendMessage(HttpServletRequest request, Message m, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int pageType) {
        //pageType: 1 = userHome; 0 = userIdeas;
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
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String postedDate = df.format(today);
        m.setSend_date(postedDate);
        messageService.addMessage(m);

        String uri = request.getRequestURI();
        if (pageType == 0) {
            return "redirect:/user/userIdeas/?page=" + page + "&userId=" + idReceiver;
        } else {
            return "redirect:/user/home/?page=" + page;
        }
    }

    @PostMapping("/sendNewMessage")
    public String sendNewMessage(HttpServletRequest request, @Valid Message m, BindingResult result) {
        //pageType: 1 = userHome; 0 = userIdeas;
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
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String postedDate = df.format(today);
        m.setSend_date(postedDate);
        messageService.addMessage(m);

        assert findConversation != null;
        return "redirect:/user/messages/" + findConversation.getId();
    }

    @PostMapping("/deleteIdea")
    public String deleteIdea(Idea ideaDelete, @RequestParam(defaultValue = "0") int page, RedirectAttributes redir) {
        // delete all info that corresponds to this idea

        Page<Idea> ideas = ideaService.getAllIdeas(page);

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

        //for notification
        redir.addFlashAttribute("deleted", "true");

        // deleting the idea's image
        if (!idea.getImage_path().contains("idea7.jpg")) {
            try {
                pictureLoaderService.deletePictureFromDisk(idea.getImage_path());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ideas.getContent().size() > 1) { // daca am macar 2 elemente pe pagina
            return "redirect:/user/home/?page=" + page;
        } else {
            if (page > 0) {
                page -= 1;
            }
            return "redirect:/user/home/?page=" + page;
        }
    }

    @PostMapping("/deleteMyIdea")
    public String deleteMyIdea(Idea ideaDelete, @RequestParam(defaultValue = "0") int page, RedirectAttributes redir) {
        // delete all info that corresponds to this idea

        // ne trebuie pentru a sti daca ramanem sau nu pe pagina asta dupa ce facem stergerea
        Page<Idea> ideas = ideaService.getIdeasByUser(page, getCurrentUser());

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

        //for notification
        redir.addFlashAttribute("deleted", "true");

        // deleting the idea's image
        if (!idea.getImage_path().contains("idea7.jpg")) {
            try {
                pictureLoaderService.deletePictureFromDisk(idea.getImage_path());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ideas.getContent().size() > 1) { // daca am macar 2 elemente pe pagina
            return "redirect:/user/myIdeas/?page=" + page;
        } else {
            if (page > 0) {
                page -= 1;
            }
            return "redirect:/user/myIdeas/?page=" + page;
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(User userDelete) throws IOException {

        User user = userRepository.getById(userDelete.getId());

        // delete all info from user's ideas
        List<Idea> userIdeas = ideaRepository.getIdeasObjectsByUser(user);
        for (Idea idea : userIdeas) {
            //delete matchings (verify if it idea or the matching idea)
            matchService.deleteMatchingsByIdea(idea);
            //delete comments
            commentsService.deleteCommentsByIdea(idea);
            //delete appreciations
            appreciationService.deleteAppreciationsByIdea(idea);
            //delete tags
            tagService.deleteTagsByIdea(idea);

            // deleting the idea's image
            if (!idea.getImage_path().contains("idea7.jpg")) {
                try {
                    pictureLoaderService.deletePictureFromDisk(idea.getImage_path());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ideaService.deleteIdea(idea);
        }

        // delete all info that corresponds to this user
        userService.deleteUserConversations(user);
        userService.deleteUserFollowings(user);
        userService.deleteUser(user);

        return "redirect:/user/dashboard/";
    }

    @PostMapping("/deleteFollowing")
    public String deleteFollowing(User userFollowing) throws IOException {

        User user = userRepository.getById(userFollowing.getId());
        User currentUser = getCurrentUser();
        List<Following> userFollowings = followingRepository.getByUser(currentUser);
        Following toDeleteFollowing = null;
        for (Following following : userFollowings) {
            if (following.getFollowingUser().equals(user)) {
                toDeleteFollowing = following;
                break;
            }
        }
        followingRepository.delete(toDeleteFollowing);

        return "redirect:/user/followings/";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(Comment com) throws IOException {

        Comment comment = commentRepository.getById(com.getId());
        Long ideaId = comment.getIdea().getId();
        commentRepository.delete(comment);
        ideaService.updateCommentsMinus(comment.getIdea());

        return "redirect:/user/viewIdea/" + ideaId;
    }

    @PostMapping("/giveRights")
    public String giveRights(User userRights, RedirectAttributes redir) throws IOException {

        User user = userRepository.getById(userRights.getId());
        // update role column
        userRepository.setNewRole(0, user.getId());
        redir.addFlashAttribute("rights", "true");
        redir.addFlashAttribute("rightName", user.getFull_name());

        return "redirect:/user/dashboard/";
    }

    @RequestMapping(value = "/myIdeas", method = RequestMethod.GET)
    public String myIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "-1") long category) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fullname", user.getFull_name());
        model.addAttribute("occupation", user.getOccupation());
        Category choseCategory = new Category();

        Page<Idea> ideas = null;
        if (category != -1) {
            choseCategory = categoryRepository.getById(category);
            ideas = ideaService.getIdeasByUserAndCategory(page, user, choseCategory);
        } else {
            ideas = ideaService.getIdeasByUser(page, user);
        }

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }
        }

        for (Idea idea : ideas) {
            if (idea.getBody().length() > 679) {
                idea.setBody(idea.getBody().substring(0, 678) + " ...");
            }
        }

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

        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> myCategoryList = categoryService.getUniqueCategoriesByUser(myIdeas);
        model.addAttribute("myCategoryList", myCategoryList);

        // construct the filteredCategoryList
        List<Category> filteredCategoryList = new ArrayList<>();
        if (category == -1) {
            filteredCategoryList = myCategoryList;
        } else {
            filteredCategoryList.add(choseCategory);
        }
        model.addAttribute("filteredCategoryList", filteredCategoryList);
        model.addAttribute("currentCategoryId", category);

        if (!model.containsAttribute("deleted")) {
            model.addAttribute("deleted", false);
        }

        return "myIdeas";
    }

    @RequestMapping(value = "/userIdeas", method = RequestMethod.GET)
    public String userIdeas(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") Long userId, @RequestParam(defaultValue = "-1") long category) {
        User user = getCurrentUser();

        User viewedUser = userRepository.getById(userId);
        model.addAttribute("currentUser", user);
        model.addAttribute("viewedUser", viewedUser);
        Category choseCategory = new Category();

        Page<Idea> ideas = null;
        if (category != -1) {
            choseCategory = categoryRepository.getById(category);
            ideas = ideaService.getIdeasByUserAndCategory(page, viewedUser, choseCategory);
        } else {
            ideas = ideaService.getIdeasByUser(page, viewedUser);
        }

        boolean ok = false;
        for (Idea idea : ideas) {
            List<Appreciation> appreciations = idea.getAppreciations();
            ok = false;
            for (Appreciation appreciation : appreciations) {
                if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                    idea.setLiked(1);
                    ok = true;
                }
            }
            if (!ok) {
                idea.setLiked(0);
            }

            for (Idea idea1 : ideas) {
                if (idea1.getBody().length() > 679) {
                    idea1.setBody(idea1.getBody().substring(0, 678) + " ...");
                }
            }
        }

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

        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("ideasList", ideas);
        model.addAttribute("currentPage", page);
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> userCategoryList = categoryService.getUniqueCategoriesByUser(ideas);
        model.addAttribute("myCategoryList", userCategoryList);

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

    @RequestMapping(value = "/postIdea", method = RequestMethod.GET)
    public ModelAndView postIdeaView(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
        if (!model.containsAttribute("largeImage")) {
            model.addAttribute("largeImage", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/postIdea", method = RequestMethod.POST)
    public ModelAndView postIdea(@Valid Idea idea, BindingResult result, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image) throws Exception {

        idea.setTitle(idea.getTitle().trim().replaceAll(" +", " "));
        idea.setBody(idea.getBody().trim().replaceAll(" +", " "));

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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
        } else if (image.getSize() <= 3000000) {

            ideaService.insert(idea, user);
            // Add matchings with this idea

            ideaService.addMatchings(idea);
            // Add tags/keywords for this idea
            ideaService.addTags(idea);
            // Send emails to the followers
            ideaService.sendEmails(user, idea);

            // image uploading on file disk
            String imagePath = "";
            if (!idea.getImage_path().equals("")) {
                imagePath = ideaService.saveIdeaImage(image, idea);
            } else {
                imagePath = "idea7.jpg";
            }
            //updating idea's path after inserting the image
            ideaService.updateImagePath(imagePath, idea);
            redir.addFlashAttribute("idCreatedIdea", idea.getId());

            modelAndView.setViewName("redirect:postIdea");
            redir.addFlashAttribute("displaySuccess", "true");

        } else {
            model.addAttribute("largeImage", "true");
            modelAndView.setViewName("postIdea");
        }

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

        return modelAndView;
    }

//    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
//    public ModelAndView uploadImage(@RequestParam("image") MultipartFile image) throws Exception {
//
//        Idea idea = ideaService.getIdeaById(146L);
//        String imagePath = "";
//        imagePath = ideaService.saveIdeaImage(image, idea);
//        ideaService.updateImagePath(imagePath, idea);
//        ModelAndView modelAndView = new ModelAndView("redirect:viewIdea/" + idea.getId());
////        ModelAndView modelAndView = new ModelAndView("redirect:home/");
//        return  modelAndView;
////        return "redirect: /viewIdea/" + idea.getId();
//    }


    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.GET)
    public String viewIdea(HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {//(@RequestParam Idea idea) {
        User user = getCurrentUser();
        model.addAttribute("currentUser", user);
        model.addAttribute("currentIdeaId", ideaId);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("userImage", "../../img/" + user.getImage());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String currentDate = df.format(today);
        model.addAttribute("commentDate", currentDate);

        //this doesn't work because page is not refreshing
//        String stringIdComm = (String) model.asMap().get("idComment");
//        model.addAttribute("idComment", stringIdComm);

        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
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

        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);

        boolean ok = false;
        List<Appreciation> appreciations = idea.getAppreciations();
        ok = false;
        for (Appreciation appreciation : appreciations) {
            if (appreciation.getUser().equals(user) && appreciation.getIdea().equals(idea)) {
                idea.setLiked(1);
                ok = true;
            }
        }
        if (!ok) {
            idea.setLiked(0);
        }

        List<Tag> allTags = idea.getTags();
        List<Tag> tags1 = new ArrayList<>();
        List<Tag> tags2 = new ArrayList<>();
        if (allTags.size() > 0) {
            for (int i = 0; i < allTags.size() / 2 + 1; i++) {
                tags1.add(allTags.get(i));
            }
            for (int i = allTags.size() / 2 + 1; i < allTags.size(); i++) {
                tags2.add(allTags.get(i));
            }
        }
        model.addAttribute("tags1", tags1);
        model.addAttribute("tags2", tags2);


        model.addAttribute("comment", new Comment());

        List<Comment> comments = idea.getComments();
        Collections.sort(comments, new Comparator<Comment>() {
            DateFormat f = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

            @Override
            public int compare(Comment o1, Comment o2) {
                try {
                    return f.parse(o1.getPosted_date()).compareTo(f.parse(o2.getPosted_date()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        Collections.reverse(comments);
        model.addAttribute("comments", comments);

        List<Idea> matchingIdeasWithScores = new ArrayList<>();
        List<Matching> matchings = idea.getMatchings();
        List<Matching> matchings2 = idea.getMatchings2();
        List<Matching> matchings2NoDuplicates = new ArrayList<>();

        // verificam daca matchings2 nu e de fapt matchings
        for (Matching matching2 : matchings2) {
            ok = true;
            for (Matching matching1 : matchings) {
                if ((matching1.getIdea().equals(matching2.getIdeaMatch()) && matching1.getIdeaMatch().equals(matching2.getIdea()))) {
                    ok = false;
                }
            }
            if (ok) {
                matchings2NoDuplicates.add(matching2);
            }
        }

        matchings.addAll(matchings2NoDuplicates);

        if (matchings.size() > 0) {
            for (Matching matching : matchings) {
                Idea idea1 = null;
                if (matching.getIdea().equals(idea)) {
                    idea1 = matching.getIdeaMatch();
                } else if (matching.getIdeaMatch().equals(idea)) {
                    idea1 = matching.getIdea();
                }

                idea1.setSemantic(matching.getSemantic());
//                idea1.setSintactic(matching.getSintactic());
                if (idea1.getBody().length() > 209) {
                    idea1.setBody(idea1.getBody().substring(0, 208) + " ...");
                }
                matchingIdeasWithScores.add(idea1);
            }
        }

        model.addAttribute(matchingIdeasWithScores);

        return "viewIdea";
    }

    @RequestMapping(value = "/viewIdea/{ideaId}", method = RequestMethod.POST)
    public String viewIdeaPostComment(@Valid Comment comment, BindingResult result, RedirectAttributes redir, HttpServletRequest httpServletRequest, @PathVariable Long ideaId, Model model) {
        User currentUser = getCurrentUser();
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

        Idea idea = ideaService.getIdeaById(ideaId);
        model.addAttribute(idea);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String postedDate = df.format(today);
        comment.setPosted_date(postedDate);
        comment.setUser(currentUser);
        comment.setIdea(idea);
        comment.setBody(comment.getBody().replaceAll(" +", " "));
        Comment addedComment = ideaService.addComment(comment);
        commentRepository.flush();
        Long idComment = addedComment.getId();
        redir.addFlashAttribute("idComment", idComment);

        ideaService.updateComments(idea);

        return "redirect:{ideaId}";
    }

    @RequestMapping(value = "/messages/{conversationId}", method = RequestMethod.GET)
    public ModelAndView myMessagesGet(HttpServletRequest httpServletRequest, @PathVariable Long conversationId, @RequestParam(defaultValue = "") String q, Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("userImage", "../../img/" + user.getImage());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        model.addAttribute("currentConversationId", conversationId);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        List<User> allUsers = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        for (User user1 : allUsers) {
            if (!user1.equals(user)) {
                userList.add(user1);
            }
        }
        model.addAttribute("userList", userList);
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

        model.addAttribute("sentMessage", new Message());

        List<Conversation> conversations = new ArrayList<>();
        if (!q.trim().toLowerCase().equals("")) {
            conversations = conversationService.getUserConversationsFiltered(user, q);
        } else {
            conversations = conversationService.getAllUserConversations(user);
        }

        Collections.sort(conversations, (c1, c2) -> {
            if (c1.getCreatedDate().equals(c2.getCreatedDate()))
                return 0;
            if (c1.getCreatedDate().before(c2.getCreatedDate()))
                return -1;
            return 1;
        });

        model.addAttribute("conversations", conversations);


        if (conversationId == -1 && conversations.size() > 0) {
            conversationId = conversations.get(0).getId();
        }
        if (conversations.size() > 0) {
            Conversation convOpen = conversationService.getById(conversationId);
            model.addAttribute("convOpen", convOpen);
            List<Message> convMessages = convOpen.getMessages();
            Collections.sort(convMessages, new Comparator<Message>() {
                DateFormat f = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                @Override
                public int compare(Message o1, Message o2) {
                    try {
                        return f.parse(o1.getSend_date()).compareTo(f.parse(o2.getSend_date()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            model.addAttribute("convMessages", convMessages);
        } else {
            model.addAttribute("convMessages", new ArrayList<>());
            model.addAttribute("convOpen", null);
        }

        ModelAndView modelAndView = new ModelAndView("messages");
        modelAndView.addObject("newMessage", new Message());

        return modelAndView;
    }

    @RequestMapping(value = "/messages/{conversationId}", method = RequestMethod.POST)
    public String myMessagesPost(@Valid Message sentMessage, BindingResult result, @PathVariable Long conversationId, HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("ideasNumber", ideaService.getIdeasByUser(user).size());
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

        List<Conversation> conversations = conversationService.getAllUserConversations(user);
        model.addAttribute("conversations", conversations);
        Conversation convOpen = conversationService.getById(conversationId);
        model.addAttribute("convOpen", convOpen);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String postedDate = df.format(today);
        sentMessage.setSend_date(postedDate);

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

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String adminDashboard(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("currentUser", currentUser);
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

        List<User> usersList = userRepository.findAllByOrderByIdDesc();
        model.addAttribute("usersList", usersList);

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(currentUser);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());

        if (!model.containsAttribute("rights")) {
            model.addAttribute("rights", false);
            model.addAttribute("rightName", "nimeni");
        }

        return "dashboard";
    }

    @RequestMapping(value = "/followings", method = RequestMethod.GET)
    public String followingUsers(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("currentUser", currentUser);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        Boolean existStudent, existDeveloper, existDirector, existTeamLeader, existFreelancer, existPrivate;
        existStudent = existDeveloper = existDirector = existTeamLeader = existFreelancer = existPrivate = false;

        List<Following> followingList = followingRepository.getByUser(currentUser);
        List<User> followingUsers = new ArrayList<>();
        for (Following following : followingList) {
            followingUsers.add(following.getFollowingUser());
            if (following.getFollowingUser().getOccupation().equals("Student")) {
                existStudent = true;
            }
            if (following.getFollowingUser().getOccupation().equals("Developer")) {
                existDeveloper = true;
            }
            if (following.getFollowingUser().getOccupation().equals("Director")) {
                existDirector = true;
            }
            if (following.getFollowingUser().getOccupation().equals("Team Leader")) {
                existTeamLeader = true;
            }
            if (following.getFollowingUser().getOccupation().equals("Freelancer")) {
                existFreelancer = true;
            }
            if (following.getFollowingUser().getOccupation().equals("Private occupation")) {
                existPrivate = true;
            }
        }

        List<User> displayedUsers = new ArrayList<>();
        for (Following following : followingList) {
            if (displayedUsers.size() < 3) {
                displayedUsers.add(following.getFollowingUser());
            }
        }
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("displayedUsers", displayedUsers);
        model.addAttribute("followingList", followingList);

        model.addAttribute("existStudent", existStudent);
        model.addAttribute("existDeveloper", existDeveloper);
        model.addAttribute("existDirector", existDirector);
        model.addAttribute("existTeamLeader", existTeamLeader);
        model.addAttribute("existFreelancer", existFreelancer);
        model.addAttribute("existPrivate", existPrivate);

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(currentUser);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("messagesNumber", currentUser.getMessages().size());

        return "followings";
    }

    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public String categoryChart(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("currentUser", user);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);


        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o2.getIdeasFromCategory().size() - o1.getIdeasFromCategory().size();
            }
        });

        // trebuie sa luam din nou ideile utilizatorului pentru ca celelalte pot fi filtrate
        List<Idea> myIdeas = ideaService.getIdeasByUser(user);
        model.addAttribute("myIdeasNumber", myIdeas.size());
        model.addAttribute("messagesNumber", user.getMessages().size());
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

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletRequest request, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
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

        return "contact";
    }

    @RequestMapping(value = "/editIdea/{ideaId}", method = RequestMethod.GET)
    public ModelAndView editIdeaGet(HttpServletRequest request, @PathVariable Long ideaId, Model model) {
        User user = getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("myIdeasNumber", ideaService.getIdeasByUser(user).size());
        model.addAttribute("messagesNumber", user.getMessages().size());
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

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

        ModelAndView modelAndView = new ModelAndView("editIdea");
        Idea editedIdea = ideaRepository.getById(ideaId);
        modelAndView.addObject("idea", editedIdea);

        if (!model.containsAttribute("displaySuccess")) {
            model.addAttribute("displaySuccess", "false");
        }
        if (!model.containsAttribute("duplicate")) {
            model.addAttribute("duplicate", "false");
        }
        if (!model.containsAttribute("format")) {
            model.addAttribute("format", "false");
        }

        if (!model.containsAttribute("largeImage")) {
            model.addAttribute("largeImage", "false");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/editIdea/{ideaId}", method = RequestMethod.POST)
    public ModelAndView editIdeaPost(@Valid Idea idea, BindingResult result, @PathVariable Long ideaId, Model model, RedirectAttributes redir, @RequestParam("file") MultipartFile image) throws Exception {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);

        User user = getCurrentUser();
        Idea currentIdea = ideaRepository.getById(ideaId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("idea", idea);

        idea.setUser(user);
        idea.setId(ideaId);
        ideaValidator.validate(idea, result);

        if (result.hasErrors()) {
            String error = result.getAllErrors().get(0).getCode();
            if (error.equals("duplicate")) {
                model.addAttribute("duplicate", "true");
            } else {
                model.addAttribute("format", "true");
            }
            modelAndView.setViewName("editIdea");
        } else if (image.getSize() <= 3000000) {
            modelAndView.setViewName("redirect:/user/editIdea/" + ideaId);

            if (!idea.getBody().equals(currentIdea.getBody()) || !idea.getCategory().equals(currentIdea.getCategory())) {
                // delete old similarities and tags and add the new ones
                matchService.deleteMatchingsByIdea(currentIdea);
                // Add matchings with this idea
                ideaService.addMatchings(idea);
                if (!idea.getBody().equals(currentIdea.getBody())) {
                    tagService.deleteTagsByIdea(currentIdea);
                    // Add tags/keywords for this idea
                    ideaService.addTags(idea);
                }
            }

            ideaService.editIdea(idea, currentIdea.getId());

            // if image was changed
            if (!currentIdea.getImage_path().equals(idea.getImage_path())) {
                // image uploading on file disk
                String imagePath = "";
                if (!idea.getImage_path().equals("")) {
                    if(!currentIdea.getImage_path().contains("idea7.jpg")){
                        pictureLoaderService.deletePictureFromDisk(currentIdea.getImage_path());
                    }
                    imagePath = ideaService.saveIdeaImage(image, idea);
                    //idea.setImage_path(imagePath);
                } else {
                    imagePath = "idea7.jpg";
                    //idea.setImage_path("idea7.jpg");
                }
                ideaService.updateImagePath(imagePath, idea);
            }

            redir.addFlashAttribute("displaySuccess", "true");
            redir.addFlashAttribute("idCreatedIdea", idea.getId());
        } else {
            model.addAttribute("largeImage", "true");
            modelAndView.setViewName("editIdea");
        }

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

        return modelAndView;
    }

}
