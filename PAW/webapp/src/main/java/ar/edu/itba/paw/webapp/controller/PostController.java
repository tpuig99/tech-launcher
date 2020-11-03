package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    PostService ps;

    private final String START_PAGE = "1";
    private final long POSTS_PAGE_SIZE = 10;

    @RequestMapping("/posts")
    public ModelAndView posts( @RequestParam(value = "page", required = false, defaultValue = START_PAGE) Long postsPage) {


        final ModelAndView mav = new ModelAndView("posts/posts_list");

        mav.addObject("posts", ps.getAll(postsPage, POSTS_PAGE_SIZE) );


        return mav;
    }

}
