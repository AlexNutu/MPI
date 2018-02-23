//package controllers;
//
//import com.alenut.mpi.entities.info.UserInformation;
//import com.alenut.mpi.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.security.NoSuchAlgorithmException;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    UserService userService;
//
//    @RequestMapping(value = "/addUser", method = RequestMethod.POST,
//            consumes = {"application/json"})
//    public String addUser(@RequestBody UserInformation userInfo) throws NoSuchAlgorithmException {
//        userService.createUser(userInfo);
//
//        return "manageUser";
//    }
//
//    @RequestMapping(value = "/editUser", method = RequestMethod.POST,
//            consumes = {"application/json"})
//    public String editUser(@RequestBody UserInformation userInfo) throws NoSuchAlgorithmException {
//        userService.editUser(userInfo);
//
//        return "manageUser";
//    }
//}
