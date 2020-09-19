package ar.edu.itba.paw.webapp.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("admin/index");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        return mav;
    }
}

