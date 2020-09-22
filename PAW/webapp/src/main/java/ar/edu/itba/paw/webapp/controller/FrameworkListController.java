package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FrameworkListController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping(path = {"/search"}, method = RequestMethod.GET)
    public ModelAndView advancedSearch(@RequestParam(value = "toSearch") final String toSearch,@RequestParam(value = "category") final String category, @RequestParam(value = "type") final String type){
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_list");
        mav.addObject("matchingFrameworks", fs.search(toSearch, FrameworkCategories.getByName(category), FrameworkType.getByName(type)));
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("search_result", toSearch );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }

        return mav;
    }
}


