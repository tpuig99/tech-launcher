package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
    @Autowired
    private UserService us;

    /*@RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(1));
        return mav;
    }*/
    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "userId", required = true) final int id) {
            final ModelAndView mav = new ModelAndView("index")
            ;
            mav.addObject("user", us.findById(id));
            return mav;
    }
    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "name", required = true) final String username,@RequestParam(value = "mail", required = true) final String mail) {
            final User u = us.create(username,mail);
            return new ModelAndView("redirect:/?userId=" + u.getId());
    }
}
