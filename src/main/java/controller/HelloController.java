
package controller;

import com.alenut.mpi.models.HelloMessage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Alenut on 7/26/2017.
 */

@RestController
public class HelloController {


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloForm(){
        return "helloform";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String hello(HttpServletRequest request, Model model) {

        //get the name parameter from request; will be null of no parameter passed in
        String name = request.getParameter("name");
        if (name == null || name.equals("")) {
            name = "World";
        }

        model.addAttribute("name", name);
        return "<h1>" + HelloMessage.getMessage(name) + "</h1>";
    }
}
