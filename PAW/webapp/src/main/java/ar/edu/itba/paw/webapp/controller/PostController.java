package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.posts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService ps;

    @Autowired
    private FrameworkService fs;

    @Autowired
    private PostCommentService pcs;

    @Autowired
    private PostTagService pts;

    @Autowired
    private UserService us;

    private final String START_PAGE = "1";
    private final long POSTS_PAGE_SIZE = 7;
    private final long COMMENTS_PAGE_SIZE = 5;
    private final int UP_VOTE_VALUE = 1;
    private final int DOWN_VOTE_VALUE = -1;

    private static ModelAndView redirectToPosts() {
        return new ModelAndView("redirect:/posts");
    }

    private static ModelAndView redirectToPost( long postId) {
        return new ModelAndView("redirect:/posts/" + postId);
    }

    @RequestMapping("/posts")
    public ModelAndView posts( @RequestParam(value = "page", required = false, defaultValue = START_PAGE) Long postsPage) {

        final ModelAndView mav = new ModelAndView("posts/posts_list");
        final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        mav.addObject("posts", ps.getAll(postsPage, POSTS_PAGE_SIZE) );
        mav.addObject("postsPage", postsPage);
        mav.addObject("pageSize", POSTS_PAGE_SIZE);
        mav.addObject("postsAmount", ps.getPostsAmount());
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("downVoteForm", new DownVoteForm());
        mav.addObject("upVoteForm", new UpVoteForm());
        mav.addObject("deletePostForm", new DeletePostForm());

        if( optionalUser.isPresent()) {
            User user = optionalUser.get();
            mav.addObject("isAdmin", user.isAdmin());
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isEnable", user.isEnable());
        }

        return mav;
    }


    @RequestMapping("/posts/{id}")
    public ModelAndView post(@PathVariable long id, @ModelAttribute("postCommentForm") final PostCommentForm form, @ModelAttribute("deletePostCommentForm") final DeletePostCommentForm deletePostCommentForm, @RequestParam(value = "page", required = false, defaultValue = START_PAGE) Long commentsPage) {

        final ModelAndView mav = new ModelAndView("posts/post");

        Optional<Post> post = ps.findById(id);
        if(post.isPresent()){
            LOGGER.info("Post {}: Requested and found, retrieving data", id);
            mav.addObject("post", post.get() );
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            mav.addObject("downVoteForm", new DownVoteForm());
            mav.addObject("upVoteForm", new UpVoteForm());
            mav.addObject("downVoteCommentForm", new DownVoteForm());
            mav.addObject("upVoteCommentForm", new UpVoteForm());
            mav.addObject("deletePostForm", new DeletePostForm());
            mav.addObject("answers", pcs.getByPost(id, commentsPage));
            mav.addObject("page", commentsPage);
            mav.addObject("page_size", COMMENTS_PAGE_SIZE);

            final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if( optionalUser.isPresent()) {
                User user = optionalUser.get();
                mav.addObject("isAdmin", user.isAdmin());
                mav.addObject("isOwner", post.get().getUser().getId().equals(user.getId()) );
                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
                mav.addObject("isEnable", user.isEnable());
            }
            return mav;
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/posts/tag/{tagName}")
    public ModelAndView postsByTagName(@PathVariable String tagName,  @RequestParam(value = "page", required = false, defaultValue = START_PAGE) Long postsPage) {

        final ModelAndView mav = new ModelAndView("posts/posts_by_tag_name");

        final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        mav.addObject("posts", ps.getByTagName( tagName,postsPage, POSTS_PAGE_SIZE) );
        mav.addObject("postsPage", postsPage);
        mav.addObject("pageSize", POSTS_PAGE_SIZE);
        mav.addObject("postsAmount", ps.getPostsAmount());
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

    @RequestMapping("/posts/add_post")
    public ModelAndView addPostPage(@ModelAttribute("addPostForm") final AddPostForm form, final BindingResult errors) {

        if(errors.hasErrors()){
            return addPostPage(form, errors);
        }
        final ModelAndView mav = new ModelAndView("posts/add_post");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        mav.addObject("categories", fs.getAllCategories());
        mav.addObject("frameworkNames", fs.getFrameworkNames());
        mav.addObject("types", fs.getAllTypes());


        return mav;
    }

    @RequestMapping(path= "/posts/addPost/add", method = RequestMethod.POST)
    public ModelAndView addPost(@Valid @ModelAttribute("addPostForm") final AddPostForm form , final BindingResult errors) {

        if(errors.hasErrors()){
            return addPostPage(form, errors);
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            Post newPost = ps.insertPost( user.get().getId(), form.getTitle(), form.getDescription() );

            for( String name : form.getNames()){
                pts.insert(name, newPost.getPostId(), PostTagType.valueOf("tech_name"));
            }
            if(form.getCategories()!=null) {
                for (String c : form.getCategories()) {
                    pts.insert(FrameworkCategories.valueOf(c).name(), newPost.getPostId(), PostTagType.valueOf("tech_category"));

                }
            }
            if(form.getTypes()!=null) {
                for (String c : form.getTypes()) {
                    pts.insert(FrameworkType.valueOf(c).name(), newPost.getPostId(), PostTagType.valueOf("tech_type"));
                }
            }

            return redirectToPost(newPost.getPostId());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/posts/edit_post/{id}", method = { RequestMethod.GET})
    public ModelAndView editPostPage(@ModelAttribute("editPostForm") final AddPostForm form, @PathVariable long id, final BindingResult errors) {

        final Optional<Post> post = ps.findById(id);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (post.isPresent()) {
            if (user.isPresent()) {
                if (post.get().getUser().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    final User u = user.get();

                    form.setPostId(id);
                    form.setTitle(post.get().getTitle());
                    form.setDescription(post.get().getDescription());
                    form.setNames(post.get().getPostTagsByType(PostTagType.tech_name));
                    form.setCategories(post.get().getPostTagsByType(PostTagType.tech_category));
                    form.setTypes(post.get().getPostTagsByType(PostTagType.tech_type));

                    ModelAndView mav = new ModelAndView("posts/edit_post");

                    mav.addObject("categories", fs.getAllCategories());
                    mav.addObject("frameworkNames", fs.getFrameworkNames());
                    mav.addObject("types", fs.getAllTypes());

                    mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

                    mav.addObject("isAdmin", u.isAdmin());
                    mav.addObject("isOwner", post.get().getUser().getId().equals(u.getId()) );
                    mav.addObject("user_isMod", u.isVerify() || u.isAdmin());
                    mav.addObject("isEnable", u.isEnable());

                    LOGGER.info("Post {}: User {} accessed the page for updating the Post", id, user.get().getId());
                    return mav;
                }

                LOGGER.error("Post {}: User without enough privileges attempted to access page for updating", id);
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Post {}: Unauthorized user attempted to access page for updating", id);
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Post {}: Requested for getting update page and not found", id);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/posts/edit_post/{id}", method = { RequestMethod.POST})
    public ModelAndView editPost(@ModelAttribute("editPostForm") final AddPostForm form, @PathVariable long id, final BindingResult errors) {

        if(errors.hasErrors()){
            return editPostPage(form, id, errors);
        }

        final Optional<Post> post = ps.findById(id);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (post.isPresent()) {
            if (user.isPresent()) {
                if (post.get().getUser().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {

                      Optional<Post> updatedPost = ps.update(id, form.getTitle(),form.getDescription());

                      if (updatedPost.isPresent()) {
                          pts.update(id, form.getNames(), form.getCategories(), form.getTypes());

                          LOGGER.info("Post {}: Updated successfully with new information", id);

                          return redirectToPost(updatedPost.get().getPostId());
                      }

                      return ErrorController.redirectToErrorView();
                }

                LOGGER.error("Post {}: User without enough privileges attempted to access page for updating", id);
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Post {}: Unauthorized user attempted to access page for updating", id);
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Post {}: Requested for getting update page and not found", id);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/posts/delete_post"}, method = RequestMethod.POST)
    public ModelAndView deleteFramework(@Valid @ModelAttribute("deletePostForm") final DeletePostForm form){
        Optional<Post> post = ps.findById(form.getPostIdx());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(post.isPresent()) {
            if (user.isPresent()) {
                if (post.get().getUser().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    ps.deletePost(form.getPostIdx());
                    LOGGER.info("Posts: Post {} deleted successfully", form.getPostIdx());
                    return new ModelAndView("redirect:/" + "posts");
                }

                LOGGER.error("Post {}: User without enough privileges attempted to delete the Post", form.getPostIdx());
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Post {}: Unauthorized user tried to delete the Post", form.getPostIdx());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Post {}: Requested for deleting Post and not found", form.getPostIdx());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/upVote/", method = RequestMethod.POST)
    public ModelAndView voteUp(@Valid @ModelAttribute("upVoteForm") final UpVoteForm form, final BindingResult errors){
        if(errors.hasErrors()){
            return  post(form.getUpVotePostId(), null, null, null);
        }
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            ps.vote(form.getUpVotePostId(), user.get().getId(), UP_VOTE_VALUE);
            return PostController.redirectToPosts();
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/{id}/upVote/", method = RequestMethod.POST)
    public ModelAndView voteUpPost(@Valid @ModelAttribute("upVoteForm") final UpVoteForm form, @PathVariable("id") long postId, final BindingResult errors ){
        if(errors.hasErrors()){
            return  post(form.getUpVotePostId(), null, null, null);
        }
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() && postId == form.getUpVotePostId()){
            ps.vote(form.getUpVotePostId(), user.get().getId(), UP_VOTE_VALUE);
            return redirectToPost(form.getUpVotePostId());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/{id}/upVoteComment/", method = RequestMethod.POST)
    public ModelAndView voteUpComment(@Valid @ModelAttribute("upVoteForm") final UpVoteForm form, @PathVariable("id") long postId, final BindingResult errors ){
        if(errors.hasErrors()){
            return  post(form.getUpVotePostId(), null, null, null);
        }
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() && postId == form.getUpVoteCommentPostId()){
            pcs.vote(form.getPostCommentUpVoteId(), user.get().getId(), UP_VOTE_VALUE);
            return redirectToPost(form.getUpVoteCommentPostId());
        }

        return ErrorController.redirectToErrorView();
    }



    @RequestMapping( path = "/posts/{id}/downVote/", method = RequestMethod.POST)
    public ModelAndView voteDown(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form,  @PathVariable("id") final long postId, final BindingResult errors){
        if(errors.hasErrors()){
            return  post(form.getDownVotePostId(), null, null, null);
        }
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() && postId == form.getDownVotePostId()){
            ps.vote(form.getDownVotePostId(), user.get().getId(), DOWN_VOTE_VALUE);
            return redirectToPost(form.getDownVotePostId());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/{id}/downVoteComment/", method = RequestMethod.POST)
    public ModelAndView voteDownComment(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form,  @PathVariable("id") long postId, final BindingResult errors){
        if(errors.hasErrors()){
            return  post(form.getDownVotePostId(), null, null, null);
        }
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() && postId == form.getDownVoteCommentPostId()){
            pcs.vote(form.getPostCommentDownVoteId(), user.get().getId(), DOWN_VOTE_VALUE);
            return redirectToPost(form.getDownVoteCommentPostId());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/downVote/", method = RequestMethod.POST)
    public ModelAndView voteDownPost(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form, final BindingResult errors){

        if(errors.hasErrors()){
            return  post(form.getDownVotePostId(), null, null, null);
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            ps.vote(form.getDownVotePostId(), user.get().getId(), DOWN_VOTE_VALUE);
            return PostController.redirectToPosts();
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = "/posts/comment", method = RequestMethod.POST )
    public ModelAndView commentPost(@Valid @ModelAttribute("postCommentForm") final PostCommentForm form, final BindingResult errors){

        if(errors.hasErrors()){
            return  post(form.getCommentPostId(), form, null, null);
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() ){
            pcs.insertPostComment(form.getCommentPostId(), user.get().getId(), form.getComment(), null);
            return redirectToPost(form.getCommentPostId());
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = "/posts/{postId}/comment/delete", method = RequestMethod.POST)
    public ModelAndView deletePostComment(@PathVariable final long postId, @Valid @ModelAttribute("deletePostCommentForm") final DeletePostCommentForm form , final BindingResult errors){
        if( errors.hasErrors() ){
            return post(form.getCommentDeletePostId(), null, form, null);
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( user.isPresent() && postId == form.getCommentDeletePostId()){
            pcs.deletePostComment(form.getCommentDeleteId());
            return redirectToPost(form.getCommentDeletePostId());
        }
        return ErrorController.redirectToErrorView();
    }
}