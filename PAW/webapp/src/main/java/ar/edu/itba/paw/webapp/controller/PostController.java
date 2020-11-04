package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.PostService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.frameworks.VoteForm;
import ar.edu.itba.paw.webapp.form.posts.DownVoteForm;
import ar.edu.itba.paw.webapp.form.posts.UpVoteForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @Autowired
    PostService ps;

    @Autowired
    FrameworkService fs;

    @Autowired
    private UserService us;

    private final String START_PAGE = "1";
    private final long POSTS_PAGE_SIZE = 10;
    private final int UP_VOTE_VALUE = 1;
    private final int DOWN_VOTE_VALUE = -1;

    public static ModelAndView redirectToPosts() {
        return new ModelAndView("redirect:/posts");
    }

    @RequestMapping("/posts")
    public ModelAndView posts( @RequestParam(value = "page", required = false, defaultValue = START_PAGE) Long postsPage) {

        final ModelAndView mav = new ModelAndView("posts/posts_list");
        final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        mav.addObject("categories_sidebar", fs.getAllCategories());
        mav.addObject("posts", ps.getAll(postsPage, POSTS_PAGE_SIZE) );
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("downVoteForm", new DownVoteForm());
        mav.addObject("upVoteForm", new UpVoteForm());

        if( optionalUser.isPresent()) {
            User user = optionalUser.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isEnable", user.isEnable());
        }

        return mav;
    }


    @RequestMapping("/posts/{id}")
    public ModelAndView post(@PathVariable long id) {

        final ModelAndView mav = new ModelAndView("posts/post");

        Optional<Post> post = ps.findById(id);
        if(post.isPresent()){
            LOGGER.info("Post {}: Requested and found, retrieving data", id);
            mav.addObject("post", post.get() );
        }

        return mav;
    }

    @RequestMapping("/posts/add_post")
    public ModelAndView addPost() {

        final ModelAndView mav = new ModelAndView("posts/add_post");


        return mav;
    }

    @RequestMapping( path = "/posts/upVote/", method = RequestMethod.POST)
    public ModelAndView voteUp(@Valid @ModelAttribute("upVoteForm") final UpVoteForm form){

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            ps.vote(form.getUpVotePostId(), user.get().getId(), UP_VOTE_VALUE);
            return PostController.redirectToPosts();
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/downVote/", method = RequestMethod.POST)
    public ModelAndView voteDown(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form){

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            ps.vote(form.getDownVotePostId(), user.get().getId(), DOWN_VOTE_VALUE);
            return PostController.redirectToPosts();
        }

        return ErrorController.redirectToErrorView();
    }
}
