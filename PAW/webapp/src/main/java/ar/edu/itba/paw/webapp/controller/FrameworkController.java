package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.dto.*;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
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

    @Context
    private UriInfo uriInfo;

    private final String START_PAGE = "1";
    private final int PAGE_SIZE = 5;
    private final int CATEGORY_PAGE_SIZE = 7;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response frameworksHome() {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Framework> frameworks = fs.getBestRatedFrameworks();
        TechsHomeDTO dto = TechsHomeDTO.fromTechs(frameworks, uriInfo);
        user.ifPresent(value -> {
            dto.setInterests(fs.getUserInterests(value.getId()).stream().map((Framework framework) -> FrameworkDTO.fromExtern(framework, uriInfo)).collect(Collectors.toList()));
        });
        return Response.ok(dto).build();
    }

    @GET
    @Path("/category")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getCategories() {
        final List<String> enumCategory = Arrays.stream(FrameworkCategories.values()).map(Enum::name).collect(Collectors.toList());
        List<CategoriesDTO> dto = enumCategory.stream().map(x -> CategoriesDTO.fromSideBar(x, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<CategoriesDTO>>(dto) {
        }).build();
    }

    @GET
    @Path("/types")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getTypes() {
        List<TypesDTO> types = fs.getAllTypes().stream().map(TypesDTO::fromTypes).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TypesDTO>>(types) {
        }).build();
    }

    @GET
    @Path("/category/{category}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response frameworkMenuPaging(@PathParam("category") String category,
                                        @QueryParam("page") @DefaultValue(START_PAGE) int frameworksPage) {
        final FrameworkCategories enumCategory = FrameworkCategories.valueOf(category);
        LOGGER.info("Techs: Getting Techs by category '{}'", enumCategory.name());
        int amount = fs.getAmountByCategory(enumCategory);
        final int pages = (int) Math.ceil(((double) amount) / CATEGORY_PAGE_SIZE);
        List<Framework> frameworkList = fs.getByCategory(enumCategory, frameworksPage);
        CategoriesDTO categoriesDTO = CategoriesDTO.fromCategories(frameworkList, amount, uriInfo);
        return pagination(uriInfo, frameworksPage, pages, categoriesDTO);
    }

    private Response pagination(UriInfo uriInfo, int page, int pages, Object dto) {
        Response.ResponseBuilder response = Response.ok(dto)
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", pages).build(), "last");
        if (page < pages)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        if (page != 1)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        return response.build();
    }

    @GET
    @Path("/{id}/comment")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response commentsOfTech(@PathParam("id") long id,
                                   @QueryParam("page") @DefaultValue(START_PAGE) int page) {

        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            int amount = framework.get().getCommentsAmount();
            int pages = (int) Math.ceil((double) amount / PAGE_SIZE);
            List<CommentDTO> dto = commentService.getCommentsWithoutReferenceByFramework(id, page).stream()
                    .map((Comment comment) -> CommentDTO.fromComment(comment, uriInfo)).collect(Collectors.toList());
            return pagination(uriInfo, page, pages, new GenericEntity<List<CommentDTO>>(dto) {
            });
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/competitors")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response techCompetitors(@PathParam("id") Long id) {
        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            List<FrameworkDTO> competitors = fs.getCompetitors(framework.get()).stream().map((f) -> FrameworkDTO.fromExtern(f, uriInfo)).collect(Collectors.toList());

            if (competitors.isEmpty()) {
                return Response.noContent().build();
            }

            return Response.ok(new GenericEntity<List<FrameworkDTO>>(competitors) {
            }).build();
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/content")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response contentOfTech(@PathParam("id") long id,
                                  @QueryParam("type") String type,
                                  @QueryParam("page") @DefaultValue(START_PAGE) int page) {
        if (type == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            ContentTypes contentTypes;
            try {
                contentTypes = ContentTypes.valueOf(type);
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.CONFLICT).entity("Incorrect Type.").build();
            }
            int amount = (int) framework.get().getContentAmount(contentTypes);
            int pages = (int) Math.ceil((double) amount / PAGE_SIZE);
            List<ContentDTO> dto = contentService.getContentByFrameworkAndType(id, contentTypes, page).stream()
                    .map((Content content) -> ContentDTO.fromContent(content, uriInfo))
                    .collect(Collectors.toList());
            Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ContentDTO>>(dto) {
            })
                    .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).queryParam("type", type).build(), "first")
                    .link(uriInfo.getAbsolutePathBuilder().queryParam("page", pages).queryParam("type", type).build(), "last");
            if (page < pages)
                response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).queryParam("type", type).build(), "next");
            if (page != 1)
                response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).queryParam("type", type).build(), "prev");
            return response.build();
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response framework(@PathParam("id") long id) {

        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            FrameworkDTO dto = FrameworkDTO.fromFramework(framework.get(), uriInfo);
            Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent()) {
                Optional<FrameworkVote> vote = user.get().getVoteForFramework(id);
                vote.ifPresent(frameworkVote -> dto.setLoggedStars(frameworkVote.getStars()));
            }
            return Response.ok(dto).build();
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public boolean nameAvailable(final String tech, final Long id) {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.isPresent() && (user.get().isAdmin() || user.get().isVerify())) {
            if (tech == null) {
                return false;
            }
            Optional<Framework> framework = fs.getByName(tech);
            if (id == null)
                return !framework.isPresent();
            return !framework.filter(value -> value.getId() != id).isPresent();
        }
        return false;
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response createTech(
            @FormDataParam("name") final String name,
            @FormDataParam("category") final String category,
            @FormDataParam("type") final String type,
            @FormDataParam("description") final String description,
            @FormDataParam("introduction") final String introduction,
            @FormDataParam("picture") final byte[] picture
    ) throws IOException {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        FrameworkType fType;
        FrameworkCategories fCategory;
        if (name == null || description == null || introduction == null || picture == null || category == null || type == null) {
            return Response.status(Response.Status.CONFLICT).entity("There should not be empty inputs.").build();
        }
        try {
            fType = FrameworkType.valueOf(type);
            fCategory = FrameworkCategories.valueOf(category);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity("Category or type incorrect.").build();
        }

        if (!nameAvailable(name, null)) {
            return Response.status(Response.Status.CONFLICT).entity("Name already exists.").build();
        }
        Optional<Framework> framework = fs.create(name, fCategory, description, introduction, fType, user.get().getId(), picture);

        if (framework.isPresent()) {
            LOGGER.info("Techs: User {} added a new Tech with id: {}", user.get().getId(), framework.get().getId());
            final URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(framework.get().getId())).build();
            return Response.created(uri).build();
        }

        LOGGER.error("Techs: A problem occurred while creating the new Tech");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response updateTech(@PathParam("id") long id,
                               @FormDataParam("name") final String name,
                               @FormDataParam("category") final String category,
                               @FormDataParam("type") final String type,
                               @FormDataParam("description") final String description,
                               @FormDataParam("introduction") final String introduction,
                               @FormDataParam("picture") final byte[] picture) throws IOException {
        if( !nameAvailable(name, id)) {
            return Response.status(Response.Status.CONFLICT).entity("Name already exists.").build();
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        final Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent()) {
            if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {

                FrameworkType fType;
                FrameworkCategories fCategory;
                if (name == null || description == null || introduction == null || category == null || type == null) {
                    return Response.status(Response.Status.CONFLICT).entity("There should not be empty inputs.").build();
                }
                try {
                    fType = FrameworkType.valueOf(type);
                    fCategory = FrameworkCategories.valueOf(category);
                } catch (IllegalArgumentException e) {
                    return Response.status(Response.Status.CONFLICT).entity("Category or type incorrect.").build();
                }

                if (fs.getByName(name).isPresent() && !framework.get().getName().equals(name)) {
                    return Response.status(Response.Status.CONFLICT).entity("Name already exists.").build();
                }
                final Optional<Framework> updatedFramework = fs.update(id, name, fCategory, description, introduction, fType, picture);

                if (updatedFramework.isPresent()) {
                    LOGGER.info("Tech {}: User {} updated the Tech", id, user.get().getId());
                    return Response.ok(FrameworkDTO.fromFramework(updatedFramework.get(), uriInfo)).build();
                }

                LOGGER.error("Tech {}: A problem occurred while updating the Tech", id);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            LOGGER.error("Tech {}: User without enough privileges attempted to update tech", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("Tech {}: Requested for updating tech and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteFramework(@PathParam("id") long id) {
        Optional<Framework> framework = fs.findById(id);
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                fs.delete(id);

                LOGGER.info("Techs: Tech {} deleted successfully", id);
                return Response.noContent().build();
            }

            LOGGER.error("Tech {}: User without enough privileges attempted to delete the Tech", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("Tech {}: Requested for deleting Tech and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/stars")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response rateTech(@PathParam("id") long id, final VoteDTO vote) {

        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (vote == null || vote.getCount() == null) {
                return Response.status(Response.Status.CONFLICT).entity("There should not be empty inputs.").build();
            }
            frameworkVoteService.insert(framework.get(), user.get().getId(), (int) Math.round(vote.getCount()));
            LOGGER.info("Tech {}: User {} rated the Tech", id, user.get().getId());
            /*TODO:CHECKEAR SI ASÍ SIRVE O HAY QUE LLAMAR PARA ACTUALIZAR VALOR*/
            vote.setCount(framework.get().getStars());
            return Response.ok(vote).build();
        }
        LOGGER.error("Tech {}: Requested for rating and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/jpg", "image/png", "image/gif"})
    public Response getImage(@PathParam("id") long id) throws IOException {
        Optional<Framework> framework = fs.findById(id);
        if (framework.isPresent())
            return Response.ok(framework.get().getPicture()).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comment")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response saveComment(@PathParam("id") long id, final CommentAddDTO form) {
        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (form == null || form.getDescription() == null || form.getDescription().isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity("Comment can not be empty.").build();
            }
            Comment comment = commentService.insertComment(id, user.get().getId(), form.getDescription(), null);
            return Response.ok(CommentDTO.fromComment(comment, uriInfo)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/{id}/comment/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response replyComment(@PathParam("id") long id, @PathParam("commentId") long commentId, final CommentAddDTO form) {
        final Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (form == null || form.getDescription() == null || form.getDescription().isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity("Comment can not be empty.").build();
            }
            Comment comment = commentService.insertComment(id, user.get().getId(), form.getDescription(), commentId);
            LOGGER.info("Tech {}: User {} replied comment {}", id, user.get().getId(), commentId);

            return Response.ok(CommentDTO.fromComment(comment, uriInfo)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/{id}/comment/{commentId}/upvote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteUpComment(@PathParam("id") long id, @PathParam("commentId") long commentId) {
        final Optional<Framework> framework = fs.findById(id);
        Optional<Comment> comment = commentService.getById(commentId);

        if (framework.isPresent() && comment.isPresent() && comment.get().getFramework().getId() == framework.get().getId()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            Optional<CommentVote> vote = commentService.vote(commentId, user.get().getId(), 1);
            LOGGER.info("Tech {}: User {} voted up comment {}", id, user.get().getId(), commentId);
            if (vote.isPresent()) {
                VoteDTO dto = new VoteDTO();
                dto.setCount((double) vote.get().getComment().getVotesUp());
                return Response.ok(dto).build();
            }
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comment/{commentId}/downvote")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response voteDownComment(@PathParam("id") long id, @PathParam("commentId") long commentId) {
        final Optional<Framework> framework = fs.findById(id);

        Optional<Comment> comment = commentService.getById(commentId);

        if (framework.isPresent() && comment.isPresent() && comment.get().getFramework().getId() == framework.get().getId()) {
            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            Optional<CommentVote> vote = commentService.vote(commentId, user.get().getId(), -1);
            LOGGER.info("Tech {}: User {} voted down comment {}", id, user.get().getId(), commentId);
            if (vote.isPresent()) {
                VoteDTO dto = new VoteDTO();
                dto.setCount((double) vote.get().getComment().getVotesDown());
                return Response.ok(dto).build();
            }
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}/comment/{commentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteComment(@PathParam("id") long id, @PathParam("commentId") long commentId) {
        Optional<Framework> framework = fs.findById(id);
        Optional<Comment> comment = commentService.getById(commentId);
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && comment.isPresent() && comment.get().getFramework().getId() == framework.get().getId()) {
            if (user.get().isAdmin() || comment.get().getUser().getUsername().equals(user.get().getUsername())) {
                commentService.deleteComment(commentId);
                return Response.noContent().build();
            }
            LOGGER.error("Tech {}: Unauthorized user tried to delete comment", id);
            return Response.status(Response.Status.FORBIDDEN).build();

        }

        LOGGER.error("Tech {}: requested for deleting comment and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comment/{commentId}/report")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response reportComment(@PathParam("id") long id, @PathParam("commentId") long commentId, final CommentAddDTO report) {
        final Optional<Framework> framework = fs.findById(id);
        Optional<Comment> comment = commentService.getById(commentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && comment.isPresent() && comment.get().getFramework().getId() == framework.get().getId()) {
            if (user.get().getCommentsReported().stream().anyMatch(x -> x.getComment().getCommentId() == commentId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (report == null || report.getDescription() == null || report.getDescription().isEmpty())
                return Response.status(Response.Status.CONFLICT).entity("Comment can not be empty.").build();

            commentService.addReport(commentId, user.get().getId(), report.getDescription());
            LOGGER.info("Tech {}: User {} reported comment {}", id, user.get().getId(), commentId);
            return Response.ok().build();
        }

        LOGGER.error("Tech {}: requested for reporting comment and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    public boolean titleIsAvailable( long id, final String title, final String contentType) {
        if (title == null ) {
            return false;
        }
        ContentTypes type;
        try {
            type = Enum.valueOf(ContentTypes.class, contentType);
        } catch (IllegalArgumentException e) {
            return false;
        }
        List<Content> ls = contentService.getContentByFrameworkAndTypeAndTitle(id, type, title);
        return ls.isEmpty();

    }

    @POST
    @Path("/{id}/content")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response addContent(@PathParam("id") long id, final ContentAddDTO content) {
        final Optional<Framework> framework = fs.findById(id);
        if( !titleIsAvailable(id, content.getTitle(), content.getType())) {
            return Response.status(Response.Status.CONFLICT).entity("Already exists.").build();
        }
        if (framework.isPresent()) {
            if (content == null || content.getLink() == null || content.getLink().isEmpty() || content.getType() == null || content.getType().isEmpty() || content.getTitle() == null || content.getTitle().isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity("Content can not be empty.").build();
            }
            ContentTypes type;
            try {
                type = Enum.valueOf(ContentTypes.class, content.getType());
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.CONFLICT).entity("Invalid type").build();
            }

            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            String pathToContent = content.getLink();
            if (!pathToContent.contains("://")) {
                pathToContent = "http://" .concat(pathToContent);
            }

            if (!contentService.getContentByFrameworkAndTypeAndTitle(id, type, content.getTitle()).isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity("Already exists.").build();
            }

            Content insertContent = contentService.insertContent(id, user.get().getId(), content.getTitle(), pathToContent, type);

            LOGGER.info("Tech {}: User {} inserted new content", id, user.get().getId());
            return Response.ok(ContentDTO.fromContent(insertContent, uriInfo)).build();
        }

        LOGGER.error("Tech {}: requested for inserting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}/content/{contentId}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteContent(@PathParam("id") long id, @PathParam("contentId") long contentId) {

        final Optional<Framework> framework = fs.findById(id);
        final Optional<Content> content = contentService.getById(contentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && content.isPresent() && content.get().getFrameworkId() == id) {
            if (user.get().isAdmin() || user.get().isVerifyForFramework(id) || content.get().getUserName().equals(user.get().getUsername())) {
                contentService.deleteContent(contentId);
                LOGGER.info("Tech {}: User {} deleted content {}", id, user.get().getId(), contentId);
                return Response.noContent().build();
            }
            LOGGER.error("Tech {}: Unauthorized user tried to delete content", id);
            return Response.status(Response.Status.FORBIDDEN).build();

        }

        LOGGER.error("Tech {}: requested for deleting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/content/{contentId}/report")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response reportContent(@PathParam("id") long id, @PathParam("contentId") long contentId, final CommentDTO report) {
        final Optional<Framework> framework = fs.findById(id);
        Optional<Content> content = contentService.getById(contentId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent() && content.isPresent() && content.get().getFrameworkId() == id) {
            if (user.get().getContentsReported().stream().anyMatch(x -> x.getContentId() == contentId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (report == null || report.getDescription() == null || report.getDescription().isEmpty())
                return Response.status(Response.Status.CONFLICT).entity("Content can not be empty.").build();

            contentService.addReport(contentId, user.get().getId(), report.getDescription());
            LOGGER.info("Tech {}: User {} reported content {}", id, user.get().getId(), contentId);
            return Response.ok().build();
        }


        LOGGER.error("Tech {}: requested for reporting content and not found", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}

