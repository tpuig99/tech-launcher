package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.service.*;
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

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @RequestMapping("/{category}/{id}")
    public ModelAndView framework(@PathVariable long id, @PathVariable String category) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Framework framework = fs.findById(id);

        mav.addObject("framework", framework);

        mav.addObject("books", contentService.getContentByFrameworkAndType(1, ContentTypes.book));
        mav.addObject("courses", contentService.getContentByFrameworkAndType(id, ContentTypes.course));
        mav.addObject("tutorials", contentService.getContentByFrameworkAndType(id, ContentTypes.tutorial));

        mav.addObject("competitors", fs.getCompetitors(framework));

        mav.addObject("comments", commentService.getCommentsByFramework(id));

        return mav;
    }
}

