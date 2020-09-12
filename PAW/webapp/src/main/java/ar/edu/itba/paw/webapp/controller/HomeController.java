package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserService us;

    @Autowired
    private FrameworkService fs;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("frameworksList", fs.getAll() );
        return mav;
    }
    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
            return new ModelAndView("session/registerForm");
    }

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return index(form);
        }
        try {
            User registered = us.create(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (UserAlreadyExistException uaeEx) {
                ModelAndView mav = new ModelAndView("session/registerForm");
                mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
                return mav;
        }
        return new ModelAndView("redirect:/");
    }
    /*@ModelAttribute("userId")
    public Integer loggedUser(final HttpSession session)
    {
        return (Integer) session.getAttribute();
    }*/

}
