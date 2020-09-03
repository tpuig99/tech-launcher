package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrameworkController {

    @Autowired
    private FrameworkService fs;

    @RequestMapping("/framework")
    public ModelAndView framework() {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        mav.addObject("framework", "angular");
        return mav;
    }
}

