package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CustomErrorController {

    @Autowired
    private UserService us;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public ModelAndView customErrorView() {
        LOGGER.error("Arrived to Custom Error Controller");
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

        return mav;
    }
}
