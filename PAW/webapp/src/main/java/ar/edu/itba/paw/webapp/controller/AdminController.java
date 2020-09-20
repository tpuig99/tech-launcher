package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private UserService us;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("admin/index");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        return mav;
    }
}

