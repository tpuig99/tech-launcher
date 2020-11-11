package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ControllerAdvice
public class ErrorController {
    @Autowired
    private UserService us;

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    private static final String ERROR_VIEW = "/error";

    public static ModelAndView redirectToErrorView() { return new ModelAndView("redirect:" + ERROR_VIEW); }


    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        LOGGER.error("Arrived to Error Controller from Exception {}", e.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

        return mav;
    }


    @RequestMapping("/403")
    public ModelAndView forbidden() {
        ModelAndView mav = new ModelAndView("403");
        LOGGER.error("Arrived to Error 403 Handler");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

        return mav;
    }


}