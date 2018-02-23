package controllers;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.entities.info.UserInformation;
import com.alenut.mpi.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

public class BaseController {

    @Autowired
    private UserServiceImpl userService;

    @ModelAttribute("currentUserEmail")
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserInformation) {
                email = ((UserInformation) authentication.getPrincipal()).getEmail();
            } else {
                email = authentication.getName();
            }
        }
        return email;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        String userEmail = getCurrentUserEmail();
        User user = userService.getByEmail(userEmail);
        return user;
    }
}
