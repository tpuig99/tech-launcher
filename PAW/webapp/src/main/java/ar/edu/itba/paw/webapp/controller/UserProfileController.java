package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {
    @Autowired
    UserService us;

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.GET)
    public ModelAndView userProfile(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("session/user_profile");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("profile", us.findByUsername(username));
        return mav;
    }
}
