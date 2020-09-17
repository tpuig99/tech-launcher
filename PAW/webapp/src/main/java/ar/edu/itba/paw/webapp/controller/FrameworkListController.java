package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrameworkListController {

    @Autowired
    private FrameworkService fs;

    @RequestMapping(path={"/search"}, method= RequestMethod.GET)
    public ModelAndView matchedFrameworks(@RequestParam("toSearch") final String toSearch) {

        final ModelAndView mav = new ModelAndView("frameworks/frameworks_list");

        mav.addObject("matchingFrameworks", fs.getByNameOrCategory(toSearch));
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("search_result", toSearch );

        return mav;
    }
}


