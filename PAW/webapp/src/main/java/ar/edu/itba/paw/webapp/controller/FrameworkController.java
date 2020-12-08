package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("techs")
@Component
public class FrameworkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkController.class);

    @Autowired
    private FrameworkService fs;

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FrameworkVoteService frameworkVoteService;

    @Autowired
    private UserService us;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    private final String START_PAGE = "1";
    private final long PAGE_SIZE = 5;

    @GET
    @Path("/{category}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response frameworkMenuPaging(@PathParam("category") String category,
                                        @RequestParam(value = "frameworks_page", required = false, defaultValue = START_PAGE) Long frameworksPage) {
        final FrameworkCategories enumCategory = FrameworkCategories.valueOf(category);
        LOGGER.info("Techs: Getting Techs by category '{}'", enumCategory.name());
        List<Framework> frameworkList = fs.getByCategory(enumCategory, frameworksPage);
        List<FrameworkDTO> list = frameworkList.stream().map(FrameworkDTO::fromFramework).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<FrameworkDTO>>(list){}).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response framework(@PathParam("id") long id,
                              @RequestParam(value = "books_page", required = false, defaultValue = START_PAGE) Long booksPage,
                              @RequestParam(value = "courses_page", required = false, defaultValue = START_PAGE) Long coursesPage,
                              @RequestParam(value = "tutorials_page", required = false, defaultValue = START_PAGE) Long tutorialsPage,
                              @RequestParam(value = "comments_page", required = false, defaultValue = START_PAGE) Long commentsPage) {

        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            FrameworkDTO dto = FrameworkDTO.fromFramework(framework.get());
            dto.setBookDTOList(contentService.getContentByFrameworkAndType(id, ContentTypes.book, booksPage).stream()
                    .map(ContentDTO::fromContent)
                    .collect(Collectors.toList()));
            dto.setCourseDTOList(contentService.getContentByFrameworkAndType(id, ContentTypes.course, coursesPage).stream()
                    .map(ContentDTO::fromContent)
                    .collect(Collectors.toList()));
            dto.setTutorialDTOList(contentService.getContentByFrameworkAndType(id, ContentTypes.tutorial, tutorialsPage).stream()
                    .map(ContentDTO::fromContent)
                    .collect(Collectors.toList()));
            dto.setCommentDTOList(commentService.getCommentsWithoutReferenceByFramework(id, commentsPage).stream()
                    .map(CommentDTO::fromComment)
                    .collect(Collectors.toList()));

            final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if( optionalUser.isPresent()){
                User user = optionalUser.get();
                /*TODO:add user info*/
            }

            return Response.ok(dto).build();
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Path("/check-name")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response checkTechName(final CheckTechDTO tech) {
        Optional<Framework> framework = fs.getByName(tech.getName());
        if(tech.getId()==null)
            return !framework.isPresent() ? Response.ok().build() : Response.status(422).build();
        if(!framework.isPresent())
            return Response.ok().build();
        return framework.get().getId() == tech.getId()? Response.ok().build() : Response.status(422).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response createTech(final FrameworkAddDTO form) throws IOException {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            FrameworkType type = FrameworkType.valueOf(form.getType());
            FrameworkCategories category = FrameworkCategories.valueOf(form.getCategory());
            byte[] picture = form.getPicture().getBytes();
            Optional<Framework> framework = fs.create(form.getTechName(),category,form.getDescription(),form.getIntroduction(),type,user.get().getId(), picture);

            if (framework.isPresent()) {
                LOGGER.info("Techs: User {} added a new Tech with id: {}", user.get().getId(), framework.get().getId());
                final URI uri = uriInfo.getAbsolutePathBuilder()
                        .path(String.valueOf(framework.get().getId())).build();
                return Response.created(uri).build();
            }

            LOGGER.error("Techs: A problem occurred while creating the new Tech");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }

        LOGGER.error("Techs: Unauthorized user attempted to add a new Tech");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response updateTech(@PathParam("id") long id,final FrameworkAddDTO form) throws IOException {

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        final Optional<Framework> framework = fs.findById(id);
        if(framework.isPresent()) {
            if (user.isPresent()) {
                if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    FrameworkType type = FrameworkType.valueOf(form.getType());
                    FrameworkCategories category = FrameworkCategories.valueOf(form.getCategory());
                    byte[] picture = form.getPicture().getBytes();
                    final Optional<Framework> updatedFramework = fs.update(id,form.getTechName(),category,form.getDescription(),form.getIntroduction(),type,picture);

                    if (updatedFramework.isPresent()) {
                        LOGGER.info("Tech {}: User {} updated the Tech", id, user.get().getId());
                        return Response.ok(form).build();
                    }

                    LOGGER.error("Tech {}: A problem occurred while updating the Tech", id);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }

                LOGGER.error("Tech {}: User without enough privileges attempted to update tech", id);
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to update Tech", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: Requested for updating tech and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteFramework(@PathParam("id") long id){
        Optional<Framework> framework = fs.findById(id);
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(framework.isPresent()) {
            if (user.isPresent()) {
                if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    fs.delete(id);

                    LOGGER.info("Techs: Tech {} deleted successfully", id);
                    return Response.noContent().build();
                }

                LOGGER.error("Tech {}: User without enough privileges attempted to delete the Tech", id);
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to delete the Tech", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: Requested for deleting Tech and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Path("/{id}/rate")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response rateTech(@PathParam("id") long id,final VoteDTO vote) {

        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent()) {
                if(user.get().isEnable()) {
                    frameworkVoteService.insert(framework.get(), user.get().getId(), (int) Math.round(vote.getCount()));
                    LOGGER.info("Tech {}: User {} rated the Tech", id, user.get().getId());
                    /*TODO:CHECKEAR SI ASÍ SIRVE O HAY QUE LLAMAR PARA ACTUALIZAR VALOR*/
                    vote.setCount(framework.get().getStars());
                    return Response.ok(vote).build();
                }
                LOGGER.error("Tech {}: Not Allowed user tried to rate the Tech", id);
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to rate the Tech", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: Requested for rating and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    /*TODO*/
    @RequestMapping(path={"/{category}/{id}/image"}, method = RequestMethod.GET)
    public @ResponseBody byte[] getImage(@PathVariable long id,
                                         @PathVariable String category) throws IOException {
        Optional<Framework> framework = fs.findById(id);
        return framework.map(Framework::getPicture).orElse(null);
    }

    @POST
    @Path("/{id}/comment")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response saveComment(@PathParam("id") long id,final CommentDTO form) {
        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isPresent() && user.get().isEnable()) {
                commentService.insertComment(form.getFrameworkId(), user.get().getId(), form.getDescription(), form.getReferenceId());
                if (form.getReferenceId() == null)
                    LOGGER.info("Tech {}: User {} inserted a comment", form.getFrameworkId(), user.get().getId());
                else
                    LOGGER.info("Tech {}: User {} replied comment {}", form.getFrameworkId(), user.get().getId(), form.getReferenceId());

                return Response.ok(form).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to insert a comment", form.getFrameworkId());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/{id}/comment/{commentId}/upvote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteUpComment(@PathParam("id") long id, @PathParam("commentId") long commentId) {
        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent()) {
                if (user.get().isEnable()) {
                    commentService.vote(commentId, user.get().getId(), 1);
                    LOGGER.info("Tech {}: User {} voted up comment {}", id, user.get().getId(), commentId);
                    Optional<Comment> comment = commentService.getById(commentId);
                    if (comment.isPresent()) {
                        VoteDTO dto = new VoteDTO();
                        dto.setCount(Double.valueOf(comment.get().getVotesUp()));
                        return Response.ok(dto).build();
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to vote comment {}", id, commentId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Path("/{id}/comment/{commentId}/downvote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteDownComment(@PathParam("id") long id, @PathParam("commentId") long commentId) {
        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isPresent()) {
                if (user.get().isEnable()) {
                    commentService.vote(commentId, user.get().getId(), -1);
                    LOGGER.info("Tech {}: User {} voted down comment {}", id, user.get().getId(), commentId);
                    Optional<Comment> comment = commentService.getById(commentId);
                    if (comment.isPresent()) {
                        VoteDTO dto = new VoteDTO();
                        dto.setCount(Double.valueOf(comment.get().getVotesDown()));
                        return Response.ok(dto).build();
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to vote comment {}", id, commentId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @DELETE
    @Path("/{id}/comment/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteComment(@PathParam("id") long id, @PathParam("commentId") long commentId){
        Optional<Framework> framework = fs.findById(id);
        Optional<Comment> comment = commentService.getById(commentId);
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && comment.isPresent()) {
            if (user.isPresent()) {
                commentService.deleteComment(commentId);
                return Response.noContent().build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to delete comment", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: requested for deleting comment and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Path("/{id}/comment/{commentId}/report")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response reportComment(@PathParam("id") long id, @PathParam("commentId") long commentId, final ReportDTO report) {
        final Optional<Framework> framework = fs.findById(id);
        Optional<Comment> comment = commentService.getById(commentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && comment.isPresent()) {
            if(user.isPresent()) {
                commentService.addReport(commentId, user.get().getId(),report.getReportDescription());
                LOGGER.info("Tech {}: User {} reported comment {}", id, user.get().getId(), commentId);
                return Response.ok().build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to report comment", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: requested for reporting comment and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/content/check-title")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response checkTitleContent(@PathParam("id") long id, final CheckContentDTO content) {
        List<Content> ls = contentService.getContentByFrameworkAndTypeAndTitle(id,Enum.valueOf(ContentTypes.class,content.getType()),content.getTitle());
        if(ls.isEmpty()){
            return Response.ok().build();
        }
        return Response.status(422).build();
    }

    @POST
    @Path("/{id}/content")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response addContent(@PathParam("id") long id, final ContentDTO content){
        final Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            ContentTypes type = ContentTypes.valueOf(content.getType());

            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isPresent()) {
                String pathToContent = content.getLink();
                if( !pathToContent.contains("://")){
                    pathToContent = "http://".concat(pathToContent);
                }
                contentService.insertContent(id, user.get().getId(), content.getTitle(), pathToContent, type);

                LOGGER.info("Tech {}: User {} inserted new content", id, user.get().getId());
                return Response.ok(content).build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to insert content", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: requested for inserting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @DELETE
    @Path("/{id}/content/{contentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteContent(@PathParam("id") long id, @PathParam("contentId") long contentId){

        final Optional<Framework> framework = fs.findById(id);
        final Optional<Content> content = contentService.getById(contentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && content.isPresent()) {
            if (user.isPresent()) {
                contentService.deleteContent(contentId);
                LOGGER.info("Tech {}: User {} deleted content {}", id, user.get().getId(), contentId);
                return Response.noContent().build();
            }
            LOGGER.error("Tech {}: Unauthorized user tried to delete content", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: requested for deleting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @POST
    @Path("/{id}/content/{contentId}/report")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response reportContent(@PathParam("id") long id, @PathParam("contentId") long contentId, final ReportDTO report){
        final Optional<Framework> framework = fs.findById(id);
        Optional<Content> content = contentService.getById(contentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && content.isPresent()) {
            if(user.isPresent()) {
                contentService.addReport(contentId, user.get().getId(),report.getReportDescription());
                LOGGER.info("Tech {}: User {} reported content {}", id, user.get().getId(), contentId);
                return Response.ok().build();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to report content", id);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOGGER.error("Tech {}: requested for reporting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
}

