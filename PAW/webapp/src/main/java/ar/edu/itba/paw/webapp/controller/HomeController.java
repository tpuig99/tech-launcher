package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
