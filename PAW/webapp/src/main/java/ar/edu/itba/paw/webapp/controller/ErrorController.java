package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorController {
        @Autowired
        private UserService us;

        private static final String ERROR_VIEW = "/error";

        public static ModelAndView redirectToErrorView() { return new ModelAndView("redirect:" + ERROR_VIEW); }

        @ExceptionHandler(value = Exception.class)
        public ModelAndView
        defaultErrorHandler(HttpServletRequest req, Exception e) {
            // If the exception is annotated with @ResponseStatus rethrow it and let
            // the framework handle it - like the OrderNotFoundException example
            // at the start of this post.
            // AnnotationUtils is a Spring Framework utility class.
            //            if (AnnotationUtils.findAnnotation
            //                    (e.getClass(), ResponseStatus.class) != null)
            //                throw e;

            // Otherwise setup and send the user to a default error-view.
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if( us.findByUsername(username).isPresent()){
                User user = us.findByUsername(username).get();
                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            }
            return mav;
        }


    @RequestMapping("/403")
    public ModelAndView forbidden() {
        ModelAndView mav = new ModelAndView("403");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }
}