package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.register.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        return mav;
    }

    @RequestMapping("/techs")
    public ModelAndView frameworksHome() {
        final ModelAndView mav = new ModelAndView("frameworks/home");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("hottestList", fs.getBestRatedFrameworks());
        List<String> categoriesList = fs.getAllCategories();
        mav.addObject("categories_sidebar", categoriesList);
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("interestsList", fs.getUserInterests(value.getId())));
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("registerForm") final LoginForm form) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_idMod", value.isVerify() || value.isAdmin()));

        return mav;
    }



}
