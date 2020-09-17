package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserProfileController {
    @Autowired
    CommentService commentService;

    @Autowired
    ContentService contentService;

    @Autowired
    FrameworkVoteService voteService;

    @Autowired
    FrameworkService frameworkService;

    @Autowired
    UserService us;

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.GET)
    public ModelAndView userProfile(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("session/user_profile");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        long userId = us.findByUsername(username).getId();
        mav.addObject("profile", us.findById((int) userId));

        List<Comment> commentList = commentService.getCommentsByUser(userId);
        Map<Long, String> commentFrameworkName = new HashMap<>();

        for (Comment comment : commentList) {
            commentFrameworkName.put(comment.getCommentId(),frameworkService.findById(comment.getFrameworkId()).getName());
        }
        mav.addObject("frameworkNames", commentFrameworkName);
        mav.addObject("contents", contentService.getContentByUser(userId));
        mav.addObject("comments", commentService.getCommentsByUser(userId));
        mav.addObject("votes", voteService.getAllByUser(userId));
        return mav;
    }
}
