package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.PostCommentDTO;
import ar.edu.itba.paw.webapp.dto.PostDTO;
import ar.edu.itba.paw.webapp.dto.VoteDTO;
import ar.edu.itba.paw.webapp.form.posts.AddPostForm;
import ar.edu.itba.paw.webapp.form.posts.DeletePostForm;
import ar.edu.itba.paw.webapp.form.posts.DownVoteForm;
import ar.edu.itba.paw.webapp.form.posts.UpVoteForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("posts")
@Component
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

    @Context
    private UriInfo uriInfo;

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

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response posts( @QueryParam("page") @DefaultValue(START_PAGE) Long postsPage) {
        final double pages = Math.ceil(((double)ps.getPostsAmount())/POSTS_PAGE_SIZE);
        List<Post> postsList = ps.getAll(postsPage, POSTS_PAGE_SIZE);
        List<PostDTO> list = postsList.stream().map(PostDTO::fromPost).collect(Collectors.toList());
        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<PostDTO>>(list){})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page",1).build(),"first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page",pages).build(),"last");
        if(postsPage < pages)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page",postsPage+1).build(),"next");
        if(postsPage != 1)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page",postsPage-1).build(),"prev");

        return response.build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response post(@PathParam("id") long id, @QueryParam(value = "page") @DefaultValue(START_PAGE) Long commentsPage) {
        Optional<Post> post = ps.findById(id);
        if(post.isPresent()) {
            LOGGER.info("Post {}: Requested and found, retrieving data", id);
            PostDTO dto = PostDTO.fromPost(post.get());
            dto.setPostComments(pcs.getByPost(post.get().getPostId(), commentsPage).stream()
                .map(PostCommentDTO::fromComment)
                .collect(Collectors.toList()));

            final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if( optionalUser.isPresent()){
                User user = optionalUser.get();
            }

            return Response.ok(dto).build();
        }
        LOGGER.error("Post {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response addPost(final AddPostForm form) {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()) {
            Post newPost = ps.insertPost( user.get().getId(), form.getTitle(), form.getDescription() );
            if(form.getNames() != null ) {
                for (String name : form.getNames()) {
                    pts.insert(name, newPost.getPostId(), PostTagType.valueOf("tech_name"));
                }
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
            final URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(newPost.getPostId())).build();
            return Response.created(uri).build();
        }

        LOGGER.error("Posts: Unauthorized user attempted to add a new Post");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response editPost(final AddPostForm form, @PathParam("id") long id) {
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        final Optional<Post> post = ps.findById(id);
        if (post.isPresent()) {
            if (user.isPresent()) {
                if (post.get().getUser().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    Optional<Post> updatedPost = ps.update(id, form.getTitle(),form.getDescription());

                    if (updatedPost.isPresent()) {
                        List<String> names = form.getNames() == null ? Collections.emptyList() : form.getNames();
                        List<String> categories = form.getCategories() == null ? Collections.emptyList() : form.getCategories();
                        List<String> types = form.getTypes() == null ? Collections.emptyList() : form.getTypes();

                        pts.update(id, names, categories, types);

                        LOGGER.info("Post {}: Updated successfully with new information", id);

                        return Response.ok(form).build();
                    }
                    LOGGER.error("Posts: A problem occurred while creating the new Post");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
                LOGGER.error("Post {}: Unauthorized user attempted to access page for updating", id);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            LOGGER.error("Post {}: Unauthorized user tried to update Post", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        LOGGER.error("Post {}: Requested for getting update page and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deletePost(final DeletePostForm form){
        Optional<Post> post = ps.findById(form.getPostIdx());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(post.isPresent()) {
            if (user.isPresent()) {
                if (post.get().getUser().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    ps.deletePost(form.getPostIdx());
                    LOGGER.info("Posts: Post {} deleted successfully", form.getPostIdx());
                    return Response.noContent().build();
                }

                LOGGER.error("Post {}: User without enough privileges attempted to delete the Post", form.getPostIdx());
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Post {}: Unauthorized user tried to delete the Post", form.getPostIdx());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Post {}: Requested for deleting Post and not found", form.getPostIdx());
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/up_vote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteUpPost(final UpVoteForm form, @PathParam("id") long postId){
        final Optional<Post> post = ps.findById(postId);
        if( post.isPresent() ){
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if( user.isPresent() && postId == form.getUpVotePostId()){
                ps.vote(form.getUpVotePostId(), user.get().getId(), UP_VOTE_VALUE);
                LOGGER.info("Post {}: User {} voted up post",postId, user.get().getId());
                PostDTO postDTO = PostDTO.fromPost(post.get());
                postDTO.setVotesUp(postDTO.getVotesUp() + 1);
                return Response.ok(postDTO).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/down_vote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteDownPost(final DownVoteForm form, @PathParam("id") long postId){
        final Optional<Post> post = ps.findById(postId);
        if( post.isPresent() ){
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if( user.isPresent() && postId == form.getDownVotePostId()){
                ps.vote(form.getDownVotePostId(), user.get().getId(), DOWN_VOTE_VALUE);
                LOGGER.info("Post {}: User {} voted down post", postId, user.get().getId());
                PostDTO postDTO = PostDTO.fromPost(post.get());
                postDTO.setVotesUp(postDTO.getVotesDown() + 1);
                return Response.ok(postDTO).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comment/{commentId}/up_vote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteUpComment(final UpVoteForm form, @PathParam("id") long postId, @PathParam("commentId") long commentId) {
        final Optional<Post> post = ps.findById(postId);
        if( post.isPresent() ) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent() && postId == form.getUpVoteCommentPostId()) {
                pcs.vote(form.getPostCommentUpVoteId(), user.get().getId(), UP_VOTE_VALUE);
                return getVoteCommentResponse(commentId);
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comment/{commentId}/down_vote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteDownComment(final DownVoteForm form,  @PathParam("id") long postId, @PathParam("commentId") long commentId){
        final Optional<Post> post = ps.findById(postId);
        if( post.isPresent() ) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent() && postId == form.getDownVoteCommentPostId()) {
                pcs.vote(form.getPostCommentDownVoteId(), user.get().getId(), UP_VOTE_VALUE);
                return getVoteCommentResponse(commentId);
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Response getVoteCommentResponse( long commentId) {
        Optional<PostComment> comment = pcs.getById(commentId);
        if (comment.isPresent()) {
            VoteDTO dto = new VoteDTO();
            dto.setCount(Double.valueOf(comment.get().getVotesUp()));
            return Response.ok(dto).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }


    @POST
    @Path("/{id}/comment")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response commentPost(final PostCommentDTO form, @PathParam("id") long postId){
        final Optional<Post> post = ps.findById(postId);
        if( post.isPresent() ){
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if( user.isPresent() ){
                pcs.insertPostComment(form.getPostId(), user.get().getId(), form.getDescription(), null);
                return Response.ok(form).build();
            }
            LOGGER.error("Post {}: Unauthorized user tried to insert a comment", postId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}/comment/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deletePostComment(@PathParam("id") final long postId, @PathParam("commentId") final long commentId, final PostCommentDTO form) {
        final Optional<Post> post = ps.findById(postId);
        if (post.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent() && postId == form.getPostId()) {
                pcs.deletePostComment(form.getPostCommentId());
                return Response.noContent().build();
            }
            LOGGER.error("Post {}: Unauthorized user tried to delete comment", postId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        LOGGER.error("Post {}: requested for deleting comment and not found", postId);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}