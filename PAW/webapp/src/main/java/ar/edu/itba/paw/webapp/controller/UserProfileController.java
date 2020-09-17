package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkVote;
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

        final List<Comment> commentList = commentService.getCommentsByUser(userId);
        Map<Long, String> frameworkCommentNames = new HashMap<>();
        for (Comment comment : commentList) {
            frameworkCommentNames.put(comment.getCommentId(),frameworkService.findById(comment.getFrameworkId()).getName());
        }

        final List<Content> contentList = contentService.getContentByUser(userId);
        Map<Long, String> frameworkContentNames = new HashMap<>();
        for (Content content : contentList) {
            frameworkContentNames.put(content.getContentId(),frameworkService.findById(content.getFrameworkId()).getName());
        }

        final List<FrameworkVote> votesList = voteService.getAllByUser(userId);
        Map<Long, String> frameworkVoteNames = new HashMap<>();
        for (FrameworkVote vote : votesList) {
            frameworkVoteNames.put(vote.getVoteId(),frameworkService.findById(vote.getFrameworkId()).getName());
        }


        mav.addObject("frameworkCommentNames", frameworkCommentNames);
        mav.addObject("frameworkContentNames", frameworkContentNames);
        mav.addObject("frameworkVoteNames", frameworkVoteNames);
        mav.addObject("contents", contentList);
        mav.addObject("comments", commentList);
        mav.addObject("votes", votesList);
        return mav;
    }
}
