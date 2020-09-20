package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrameworkMenuController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping("/{category}")
    public ModelAndView frameworkMenu(@PathVariable String category) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");
        mav.addObject("category",category);
        mav.addObject("frameworksList", fs.getByCategory(FrameworkCategories.getByName(category)));
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        mav.addObject("user_isMod", user.isVerify() || user.isAdmin());

        return mav;
    }
}