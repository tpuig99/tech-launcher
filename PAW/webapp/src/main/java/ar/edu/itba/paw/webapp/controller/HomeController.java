package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.service.FrameworkService;

@Controller
public class HomeController {
    @Autowired
    private FrameworkService fs;

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        return mav;
    }

    @RequestMapping("/frameworks")
    public ModelAndView frameworksHome() {
        final ModelAndView mav = new ModelAndView("frameworks/home");
        mav.addObject("frameworksList", fs.getAll().subList(0, 5));
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("registerForm") final UserForm form) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        return mav;
    }
}
