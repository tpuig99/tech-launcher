package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;

@Controller
public class FrameworkMenuController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    final private long startPage = 1;
    final private long PAGESIZE = 7;

    @RequestMapping("/{category}")
    public ModelAndView frameworkMenu(@PathVariable String category) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");

        if (!fs.getByCategory(FrameworkCategories.getByName(category), startPage).isEmpty()) {
            mav.addObject("category",category);
            mav.addObject("frameworksList", fs.getByCategory(FrameworkCategories.getByName(category), startPage));
            mav.addObject("frameworks_page", startPage);
            mav.addObject("page_size", PAGESIZE);
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
    @RequestMapping("/{category}/pages")
    public ModelAndView frameworkMenuPaging(@PathVariable String category, @RequestParam("frameworks_page") final long frameworksPage) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");

        if (!fs.getByCategory(FrameworkCategories.getByName(category),frameworksPage).isEmpty()) {
            mav.addObject("category",category);
            mav.addObject("frameworksList", fs.getByCategory(FrameworkCategories.getByName(category),frameworksPage));
            mav.addObject("frameworks_page", frameworksPage);
            mav.addObject("page_size", PAGESIZE);
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