package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InvalidURLController {
    @RequestMapping("/error")
    public ModelAndView invalid(@RequestParam("invalidCode") final String invalidCode) {
        final ModelAndView mav = new ModelAndView("invalid/invalid");
        mav.addObject("error", invalidCode );
        return mav;
    }
}
