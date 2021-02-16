package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.ReportDTO;
import ar.edu.itba.paw.webapp.dto.VerifyUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("mod")
@Component
public class ModController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModController.class);

    @Autowired
    private UserService us;

    @Autowired
    private FrameworkService fs;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ContentService contentService;

    @Context
    private UriInfo uriInfo;

    private final long PAGE_SIZE = 5;
    private final String START_PAGE = "1";

    private Response.ResponseBuilder addPaginationLinks(Response.ResponseBuilder responseBuilder, long currentPage, long pages) {
        responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", pages).build(), "last");
        if (currentPage < pages) {
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage + 1).build(), "next");
        }
        if (currentPage != 1) {
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage - 1).build(), "prev");
        }

        return responseBuilder;
    }

    @GET
    @Path("/moderators")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response moderators(@QueryParam("page") @DefaultValue(START_PAGE) Long modsPage) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int modsAmount = 0;
        List<VerifyUser> userList = Collections.emptyList();
        List<VerifyUserDTO> verifyUserDTOList;

        if (us.isAdmin(user)) {
            userList = us.getVerifyByPending(false, modsPage);
            modsAmount = us.getVerifyByPendingAmount(false).get();

        } else if (us.isOwner(user)) {
            List<Long> frameworkIds = new ArrayList<>();
            List<Long> frameworkIdsForReportedComments = new ArrayList<>();
            us.getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);
            userList = us.getVerifyByPendingAndFrameworks(false, frameworkIdsForReportedComments, modsPage);
            modsAmount = us.getVerifyByPendingAndFrameworksAmount(false, frameworkIdsForReportedComments);
        }

        return createUserListResponse(modsPage, modsAmount, userList);
    }

    private Response createUserListResponse(@DefaultValue(START_PAGE) @QueryParam("page") Long modsPage, double modsAmount, List<VerifyUser> userList) {
        List<VerifyUserDTO> verifyUserDTOList;
        if (userList.size() > 0) {
            verifyUserDTOList = userList.stream().map((VerifyUser verifyUser) -> VerifyUserDTO.fromVerifyUser(verifyUser,uriInfo)).collect(Collectors.toList());
            long pages = (long) Math.ceil(modsAmount / PAGE_SIZE);
            Response.ResponseBuilder response = Response.ok(new GenericEntity<List<VerifyUserDTO>>(verifyUserDTOList) {
            });
            return addPaginationLinks(response, modsPage, pages).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/applicants")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response applicants(@QueryParam("page") @DefaultValue(START_PAGE) Long applicantsPage) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int modsAmount = 0;
        List<VerifyUser> userList = Collections.emptyList();
        List<VerifyUserDTO> verifyUserDTOList;

        if (us.isAdmin(user)) {
            userList = us.getApplicantsByPending(true, applicantsPage);
            modsAmount = us.getApplicantsByPendingAmount(true).get();

        } else if (us.isOwner(user) || us.isVerify(user)) {
            List<Long> frameworkIds = new ArrayList<>();
            List<Long> frameworkIdsForReportedComments = new ArrayList<>();
            us.getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

            userList = us.getApplicantsByFrameworks(frameworkIds, applicantsPage);
            modsAmount = us.getApplicantsByFrameworkAmount(frameworkIds, true).get();
        }

        return createUserListResponse(applicantsPage, modsAmount, userList);
    }

    @GET
    @Path("/verified")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response verified(@QueryParam("page") @DefaultValue(START_PAGE) Long verifyPage) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int modsAmount = 0;
        List<VerifyUser> userList = Collections.emptyList();
        List<VerifyUserDTO> verifyUserDTOList = null;

        if (us.isAdmin(user)) {
            userList = us.getVerifyByPending(true, verifyPage);
            modsAmount = us.getVerifyByPendingAmount(true).get();

        } else if (us.isOwner(user) || us.isVerify(user)) {
            List<Long> frameworkIds = new ArrayList<>();
            List<Long> frameworkIdsForReportedComments = new ArrayList<>();
            us.getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

            userList = us.getVerifyByPendingAndFrameworks(true, frameworkIdsForReportedComments, verifyPage);
            modsAmount = us.getVerifyByPendingAndFrameworksAmount(true, frameworkIdsForReportedComments);
        }

        return createUserListResponse(verifyPage, modsAmount, userList);
    }

    @GET
    @Path("/reported_comments")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reportedComments(@QueryParam("page") @DefaultValue(START_PAGE) Long rComPage) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int reportsAmount = 0;
        List<ReportComment> commentList = Collections.emptyList();
        List<ReportDTO> reportDTOList;

        if (us.isAdmin(user)) {
            commentList = commentService.getAllReport(rComPage);
            reportsAmount = commentService.getAllReportsAmount().get();

        } else if (us.isOwner(user)) {
            List<Long> frameworkIds = new ArrayList<>();
            List<Long> frameworkIdsForReportedComments = new ArrayList<>();
            us.getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

            commentList = commentService.getReportsByFrameworks(frameworkIdsForReportedComments, rComPage);
            reportsAmount = commentService.getReportsAmountByFrameworks(frameworkIds);
        }

        if (commentList.size() > 0) {
            reportDTOList = commentList.stream().map((reportedComment) -> ReportDTO.fromReportComment(reportedComment, uriInfo)).collect(Collectors.toList());
            long pages = (long) Math.ceil(((double) reportsAmount) / PAGE_SIZE);
            Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOList) {
            });
            return addPaginationLinks(response, rComPage, pages).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/reported_contents")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reportedContents(@QueryParam("page") @DefaultValue(START_PAGE) Long rConPage) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int reportsAmount = 0;
        List<ReportContent> contentList = Collections.emptyList();
        List<ReportDTO> reportDTOList;

        if (us.isAdmin(user)) {
            contentList = contentService.getAllReports(rConPage);
            reportsAmount = contentService.getAllReportsAmount().get();

        } else if (us.isOwner(user) || us.isVerify(user)) {
            List<Long> frameworkIds = new ArrayList<>();
            List<Long> frameworkIdsForReportedComments = new ArrayList<>();
            us.getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

            contentList = contentService.getReportsByFrameworks(frameworkIds, rConPage);
            reportsAmount = contentService.getReportsAmount(frameworkIds).get();
        }

        if (contentList.size() > 0) {
            reportDTOList = contentList.stream().map((reportedContent) -> ReportDTO.fromReportContent(reportedContent, uriInfo)).collect(Collectors.toList());
            long pages = (long) Math.ceil(((double) reportsAmount) / PAGE_SIZE);
            Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOList) {
            });
            return addPaginationLinks(response, rConPage, pages).build();
        }

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response demoteMod(@PathParam("id") Long verificationId) {
        Optional<User> user = us.findByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        Optional<VerifyUser> vu = us.getVerifyById(verificationId);

        if (user.isPresent() && (us.isAdmin(user.get()) || !us.isOwner(user.get()))) {
            if (vu.isPresent()) {
                if (!vu.get().isPending()) {
                    us.deleteVerification(verificationId);
                    LOGGER.info("User: Demoted user according to Verification {}", verificationId);
                    return Response.ok().build();
                }
                return Response.notModified().build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/pending/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptMod(@PathParam("id") Long verificationId) {
        Optional<User> user = us.findByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        Optional<VerifyUser> vu = us.getVerifyById(verificationId);
        if (user.isPresent() && (us.isAdmin(user.get()) || !us.isOwner(user.get()))) {
            if (vu.isPresent()) {
                if (vu.get().isPending()) {
                    us.verify(verificationId);
                    Optional<User> u = us.findById(vu.get().getUserId());
                    u.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
                    return Response.ok().build();
                }
                return Response.notModified().build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("/pending/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response rejectMod(@PathParam("id") Long verificationId) {
        Optional<User> user = us.findByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        Optional<VerifyUser> vu = us.getVerifyById(verificationId);

        if (user.isPresent() && (us.isAdmin(user.get()) || !us.isOwner(user.get()))) {
            if (vu.isPresent() && vu.get().isPending()) {
                if (vu.get().isPending()) {
                    us.deleteVerification(verificationId);
                    return Response.ok().build();
                }
                return Response.notModified().build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/tech/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response applyForTech(@PathParam("id") Long techId) {
        Optional<User> user = us.findByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        if ( user.isPresent() && !user.get().hasAppliedToFramework(techId)) {
            Optional<Framework> framework = fs.findById(techId);
            if (framework.isPresent()) {
                us.createVerify(user.get(), framework.get());
                return Response.ok().build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("/tech/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response quitModdingFromTech(@PathParam("id") Long techId) {
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.isPresent() && us.quitModdingFromTech(user.get(), techId)) {
            LOGGER.info("Tech {}: User {} is no longer a Mod", techId, user.get().getId());
            return Response.ok().build();
        }
        LOGGER.error("Tech {}: User {} tried to quit from being a Mod without previously being one", techId, user.get().getId());
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/reports/comment/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteCommentReport(@PathParam("id") Long commentId) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (us.isAdmin(user) || us.isOwner(user)) {
            commentService.denyReport(commentId);
            LOGGER.info("User: Comment {} was removed from its report", commentId);
            return Response.ok().build();
        }
        LOGGER.error("User: User {} has not enough privileges to ignore a reported comment", user.getId());
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/reports/comment/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptCommentReport(@PathParam("id") Long commentId) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (us.isAdmin(user) || us.isOwner(user)) {
            Optional<Comment> commentOptional = commentService.getById(commentId);
            if (commentOptional.isPresent()) {
                commentService.acceptReport(commentId);
                LOGGER.info("User: Comment {} was successfully deleted", commentId);
                return Response.ok().build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOGGER.error("User: User {} has not enough privileges to ignore a reported comment", user.getId());
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("/reports/content/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteContentReport(@PathParam("id") Long contentId) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (us.isAdmin(user) || us.isOwner(user)) {
            contentService.denyReport(contentId);
            LOGGER.info("User: Content {} was removed from its report", contentId);
            return Response.ok().build();
        }
        LOGGER.error("User: User {} has not enough privileges to ignore a reported content", user.getId());
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/reports/content/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptContentReport(@PathParam("id") Long contentId) {
        User user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (us.isAdmin(user) || us.isOwner(user)) {
            Optional<Content> contentOptional = contentService.getById(contentId);
            if (contentOptional.isPresent()) {
                contentService.acceptReport(contentId);
                LOGGER.info("User: Content {} was successfully deleted", contentId);
                return Response.ok().build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOGGER.error("User: User {} has not enough privileges to ignore a reported content", user.getId());
        return Response.status(Response.Status.FORBIDDEN).build();
    }


}
