package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.register.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.service.FrameworkService;

import java.util.ArrayList;
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
        return new ModelAndView("redirect:/" + "frameworks");
    }

    @RequestMapping("/frameworks")
    public ModelAndView frameworksHome() {
        final ModelAndView mav = new ModelAndView("frameworks/home");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("hottestList", fs.getBestRatedFrameworks());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("interestsList", fs.getUserInterests(value.getId())));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( us.findByUsername(username).isPresent()){
            User user1 = us.findByUsername(username).get();
            mav.addObject("user_isMod", user1.isVerify() || user1.isAdmin());
        }
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("registerForm") final LoginForm form) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SimpleGrantedAuthority> aut = new ArrayList<>();
        aut.add(new SimpleGrantedAuthority("ROLE_USER"));

        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }



}
