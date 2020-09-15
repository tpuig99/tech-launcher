package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
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
        mav.addObject("frameworksList", fs.getAll() );
        return mav;
    }
    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("registerForm") final UserForm form) {
        return new ModelAndView("login");
    }
}
