package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Component
public class UserProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private FrameworkVoteService voteService;

    @Autowired
    private FrameworkService frameworkService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    final private long PAGE_SIZE = 5;
    final private long FRAMEWORK_PAGE_SIZE = 7;
    final private long VOTE_PAGE_SIZE = 10;
    final private String START_PAGE = "1";

    private Response.ResponseBuilder addPaginationLinks (Response.ResponseBuilder responseBuilder, String parameterName, double currentPage, double pages) {
        responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam(parameterName,1).build(),"first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam(parameterName,pages).build(),"last");
        if(currentPage < pages) {
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam(parameterName, currentPage + 1).build(), "next");
        }
        if(currentPage != 1) {
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam(parameterName, currentPage - 1).build(), "prev");
        }

        return responseBuilder;
    }

    @GET
    @Path("{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUser(@PathParam("id") Long userId) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {

            LOGGER.info("User Profile: Requested user {} exists, retrieving data", userId);

            UserDTO dto = UserDTO.fromUser(user.get());

            LOGGER.info("User Profile: User {} updated its profile successfully", user.get().getId());
            return Response.ok(dto).build();
        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/comments")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserComments (@PathParam("id") Long userId, @QueryParam("comments_page") Long commentsPage) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {

            final List<Comment> commentsList = commentService.getCommentsByUser(userId, commentsPage);
            final Optional<Integer> commentsAmount = commentService.getCommentsCountByUser(userId);

            if(commentsList.size() > 0) {
                List<CommentDTO> commentDTOList = commentsList.stream().map(CommentDTO::fromComment).collect(Collectors.toList());
                double pages = Math.ceil(((double) commentsAmount.get()) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<CommentDTO>>(commentDTOList){});
                return addPaginationLinks(response, "comments_page", commentsPage, pages).build();
            }

            return Response.noContent().build();

        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/contents")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserContents (@PathParam("id") Long userId, @QueryParam("contents_page") Long contentsPage) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            final List<Content> contentsList = contentService.getContentByUser(userId, contentsPage);
            final Optional<Long> contentsAmount = contentService.getContentCountByUser(userId);

            if(contentsList.size() > 0) {
                List<ContentDTO> contentDTOList = contentsList.stream().map(ContentDTO::fromContent).collect(Collectors.toList());
                double pages = Math.ceil(((double) contentsAmount.get()) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ContentDTO>>(contentDTOList){});
                return addPaginationLinks(response, "contents_page", contentsPage, pages).build();
            }

            return Response.noContent().build();
        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/posts")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserPosts (@PathParam("id") Long userId, @QueryParam("posts_page") Long postsPage) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            final List<Post> postsList = postService.getPostsByUser(userId, postsPage);
            final Optional<Integer> postsAmount = postService.getPostsCountByUser(userId);

            if(postsList.size() > 0) {
                List<PostDTO> postDTOList = postsList.stream().map(PostDTO::fromPost).collect(Collectors.toList());
                double pages = Math.ceil(((double) postsAmount.get()) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<PostDTO>>(postDTOList){});
                return addPaginationLinks(response, "posts_page", postsPage, pages).build();
            }

            return Response.noContent().build();
        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/votes")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserVotes (@PathParam("id") Long userId, @QueryParam("votes_page") Long votesPage) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            final List<FrameworkVote> votesList = voteService.getAllByUser(userId, votesPage);
            final Optional<Integer> votesAmount = voteService.getAllCountByUser(userId);

            if(votesList.size() > 0) {
                List<VoteDTO> voteDTOList = votesList.stream().map(VoteDTO::fromFrameworkVote).collect(Collectors.toList());
                double pages = Math.ceil(((double) votesAmount.get()) / VOTE_PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<VoteDTO>>(voteDTOList){});
                return addPaginationLinks(response, "votes_page", votesPage, pages).build();
            }

            return Response.noContent().build();
        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/techs")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserTechs (@PathParam("id") Long userId,  @QueryParam("techs_page") Long techsPage) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            final List<Framework> techsList = frameworkService.getByUser(userId, techsPage);
            final Optional<Integer> techsAmount = frameworkService.getByUserCount(userId);

            if(techsList.size() > 0) {
                List<FrameworkDTO> frameworkDTOList = techsList.stream().map(FrameworkDTO::fromFramework).collect(Collectors.toList());
                double pages = Math.ceil(((double) techsAmount.get()) / FRAMEWORK_PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<FrameworkDTO>>(frameworkDTOList){});
                return addPaginationLinks(response, "techs_page", techsPage, pages).build();
            }

            return Response.noContent().build();
        }

        LOGGER.error("User Profile: Nonexistant user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/image")
    @Produces({"image/jpg", "image/png", "image/gif"})
    public Response getImage(@PathParam("id") Long id) {
        return Response.ok(us.findById(id).map(User::getPicture).orElse(null)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response updateProfile(@PathParam("id") Long userId, final UserDTO form) throws IOException {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            if (SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {

                if (form.getPicture() != null) {
                    byte[] picture = form.getPicture();
                    us.updateInformation(userId, form.getDescription(), picture, true);
                } else {
                    us.updateInformation(userId, form.getDescription(), user.get().getPicture(), false);
                }

                LOGGER.info("User Profile: User {} updated its profile successfully", user.get().getId());
                return Response.ok(form).build();
            }
            LOGGER.error("User Profile: User {} does not have enough privileges to update profile", userId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        LOGGER.error("User Profile: Unauthorized user attempted to update another profile");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    @Path("/{id}/enable_modding/{value}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response enableMod(@PathParam("id") Long userId, @PathParam("value") Boolean value) {
        Optional<User> user = us.findById(userId);

        if (user.isPresent()) {
            if (SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {
                us.updateModAllow(userId, value);
                LOGGER.info("User Profile: User {} updated its Mod Status successfully", userId);
                return Response.ok().build();
            }
            LOGGER.error("User Profile: User {} does not have enough privileges to update the Mod Status", userId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("User Profile: Unauthorized user attempted to update a Mod Status");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}
