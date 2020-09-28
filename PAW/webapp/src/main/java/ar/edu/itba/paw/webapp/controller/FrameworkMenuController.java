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

import java.awt.*;

@Controller
public class FrameworkMenuController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping("/{category}")
    public ModelAndView frameworkMenu(@PathVariable String category) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");

        if (!fs.getByCategory(FrameworkCategories.getByName(category)).isEmpty()) {
            mav.addObject("category",category);
            mav.addObject("frameworksList", fs.getByCategory(FrameworkCategories.getByName(category)));
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if( us.findByUsername(username).isPresent()){
                User user = us.findByUsername(username).get();
                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            }
            return mav;
        }

        return ErrorController.redirectToErrorView();
    }
}