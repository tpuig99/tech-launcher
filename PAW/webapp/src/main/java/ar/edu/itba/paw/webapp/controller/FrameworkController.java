package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrameworkController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/frameworks/{id}")
    public ModelAndView framework(@PathVariable long id) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");

        mav.addObject("framework", fs.findById(id));
        mav.addObject("content", contentService.getContentByFramework(id));

        return mav;
    }
}

