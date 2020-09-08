package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrameworkController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService us;

    @RequestMapping("/{category}/{id}")
    public ModelAndView framework(@PathVariable long id, @PathVariable String category) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Framework framework = fs.findById(id);

        mav.addObject("framework", framework);

        mav.addObject("books", contentService.getContentByFrameworkAndType(id, ContentTypes.book));
        mav.addObject("courses", contentService.getContentByFrameworkAndType(id, ContentTypes.course));
        mav.addObject("tutorials", contentService.getContentByFrameworkAndType(id, ContentTypes.tutorial));
        mav.addObject("category", category);
        mav.addObject("competitors", fs.getCompetitors(framework));

        mav.addObject("comments", commentService.getCommentsByFramework(id));

        return mav;
    }

    @RequestMapping(path={"/create"}, method= RequestMethod.GET)
    public ModelAndView saveComment(@RequestParam("id") final long id, @RequestParam("content") final String content, @RequestParam("username") final String username, @RequestParam("email") final String email){
        Framework framework = fs.findById(id);
        final User user = us.create(username, email);
        final Comment comment = commentService.insertComment(framework.getId(),user.getId(),content,1);

        return new ModelAndView("redirect:/frameworks/"+framework.getId());
    }
}

