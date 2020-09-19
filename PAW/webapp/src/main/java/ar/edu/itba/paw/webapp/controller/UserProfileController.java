package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.FrameworkVote;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        if (us.findByUsername(username).isPresent()) {
            User user = us.findByUsername(username).get();
            long userId = user.getId();
            mav.addObject("profile", user);

            final List<Comment> commentList = commentService.getCommentsByUser(userId);
            final List<Content> contentList = contentService.getContentByUser(userId);
            final List<FrameworkVote> votesList = voteService.getAllByUserWithFrameworkName(userId);

            mav.addObject("contents", contentList);
            mav.addObject("comments", commentList);
            mav.addObject("votes", votesList);

            return mav;
        }

        return ErrorController.redirectToErrorView();
    }
}
