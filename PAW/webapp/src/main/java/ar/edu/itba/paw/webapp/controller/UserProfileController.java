package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {
    @Autowired
    CommentService commentService;

    @Autowired
    ContentService contentService;

    @Autowired
    FrameworkVoteService voteService;

    @Autowired
    UserService us;

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.GET)
    public ModelAndView userProfile(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("session/user_profile");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        long userId = us.findByUsername(username).getId();
        mav.addObject("profile", us.findById((int) userId));
        mav.addObject("contents", contentService.getContentByUser(userId));
        mav.addObject("comments", commentService.getCommentsByUser(userId));
        mav.addObject("votes", voteService.getAllByUser(userId));
        return mav;
    }
}
