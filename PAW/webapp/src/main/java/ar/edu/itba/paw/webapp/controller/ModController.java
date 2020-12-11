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
    private static final String DEFAULT_PROMOTE_TAB = "promote";
    private static final String DEFAULT_DEMOTE_TAB = "demote";
    private static final String DEFAULT_REPORT_TAB = "reports";

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

    private final long pageStart=1;
    private final long PAGE_SIZE=5;
    private final String START_PAGE="1";

    private static final String MOD_VIEW = "/mod?tabs=";

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

    private void getVerifiedAndOwnedFrameworks (User user, List<Long> frameworkIds, List<Long> frameworkIdsForReportedComments) {

        user.getVerifications().forEach( verifyUser -> {
            if( !verifyUser.isPending() )
                frameworkIds.add(verifyUser.getFrameworkId());
        });

        user.getOwnedFrameworks().forEach( framework -> {
            frameworkIds.add(framework.getId());
            frameworkIdsForReportedComments.add(framework.getId());
        });
    }

    @GET
    @Path("/moderators")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response moderators (@QueryParam("modsPage") @DefaultValue(START_PAGE) Long modsPage) {
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            int modsAmount = 0;
            List<VerifyUser> userList = Collections.emptyList();
            List<VerifyUserDTO> verifyUserDTOList;

            if (user.isAdmin()) {
                userList = us.getVerifyByPending(false, modsPage);
                modsAmount = us.getVerifyByPendingAmount(false).get();

            } else if (ownedFrameworks.size() > 0) {
                List<Long> frameworkIds = new ArrayList<>();
                List<Long> frameworkIdsForReportedComments = new ArrayList<>();
                getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

                userList = us.getVerifyByPendingAndFrameworks(false, frameworkIdsForReportedComments, modsPage);
                modsAmount = us.getVerifyByPendingAndFrameworksAmount(false, frameworkIdsForReportedComments);
            }

            if( userList.size() > 0) {
                verifyUserDTOList = userList.stream().map(VerifyUserDTO::fromVerifyUser).collect(Collectors.toList());
                double pages = Math.ceil(((double) modsAmount) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<VerifyUserDTO>>(verifyUserDTOList){});
                return addPaginationLinks(response, "modsPage", modsPage, pages).build();
            }

            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/applicants")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response applicants (@QueryParam("applicantsPage") @DefaultValue(START_PAGE) Long applicantsPage) {
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            int modsAmount = 0;
            List<VerifyUser> userList = Collections.emptyList();
            List<VerifyUserDTO> verifyUserDTOList;

            if (user.isAdmin()) {
                userList = us.getApplicantsByPending(true, applicantsPage);
                modsAmount = us.getApplicantsByPendingAmount(false).get();

            } else if (ownedFrameworks.size() > 0 || user.isVerify() ) {
                List<Long> frameworkIds = new ArrayList<>();
                List<Long> frameworkIdsForReportedComments = new ArrayList<>();
                getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

                userList = us.getApplicantsByFrameworks(frameworkIds, applicantsPage);
                modsAmount = us.getApplicantsByFrameworkAmount(frameworkIds, true).get();
            }

            if( userList.size() > 0) {
                verifyUserDTOList = userList.stream().map(VerifyUserDTO::fromVerifyUser).collect(Collectors.toList());
                double pages = Math.ceil(((double) modsAmount) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<VerifyUserDTO>>(verifyUserDTOList){});
                return addPaginationLinks(response, "applicantsPage", applicantsPage, pages).build();
            }

            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/verified")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response verified (@QueryParam("verifyPage") @DefaultValue(START_PAGE) Long verifyPage) {
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            int modsAmount = 0;
            List<VerifyUser> userList = Collections.emptyList();
            List<VerifyUserDTO> verifyUserDTOList = null;

            if (user.isAdmin()) {
                userList = us.getVerifyByPending(true, verifyPage);
                modsAmount = us.getVerifyByPendingAmount(true).get();

            } else if (ownedFrameworks.size() > 0 || user.isVerify() ) {
                List<Long> frameworkIds = new ArrayList<>();
                List<Long> frameworkIdsForReportedComments = new ArrayList<>();
                getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

                userList = us.getVerifyByPendingAndFrameworks(true, frameworkIdsForReportedComments, verifyPage);
                modsAmount = us.getVerifyByPendingAndFrameworksAmount(true, frameworkIdsForReportedComments);
            }

            if( userList.size() > 0) {
                verifyUserDTOList = userList.stream().map(VerifyUserDTO::fromVerifyUser).collect(Collectors.toList());
                double pages = Math.ceil(((double) modsAmount) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<VerifyUserDTO>>(verifyUserDTOList){});
                return addPaginationLinks(response, "verifyPage", verifyPage, pages).build();
            }

            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/reported_comments")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reportedComments (@QueryParam("rComPage") @DefaultValue(START_PAGE) Long rComPage) {
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            int reportsAmount = 0;
            List<ReportComment> commentList = Collections.emptyList();
            List<ReportDTO> reportDTOList;

            if (user.isAdmin()) {
                commentList = commentService.getAllReport(rComPage);
                reportsAmount = commentService.getAllReportsAmount().get();

            } else if (ownedFrameworks.size() > 0) {
                List<Long> frameworkIds = new ArrayList<>();
                List<Long> frameworkIdsForReportedComments = new ArrayList<>();
                getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

                commentList = commentService.getReportsByFrameworks(frameworkIdsForReportedComments, rComPage);
                reportsAmount = commentService.getReportsAmountByFrameworks(frameworkIds);
            }

            if( commentList.size() > 0) {
                reportDTOList = commentList.stream().map(ReportDTO::fromReportComment).collect(Collectors.toList());
                double pages = Math.ceil(((double) reportsAmount) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOList){});
                return addPaginationLinks(response, "rComPage", rComPage, pages).build();
            }

            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/reported_contents")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reportedContents (@QueryParam("rConPage") @DefaultValue(START_PAGE) Long rConPage) {
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            int reportsAmount = 0;
            List<ReportContent> contentList = Collections.emptyList();
            List<ReportDTO> reportDTOList;

            if (user.isAdmin()) {
                contentList = contentService.getAllReports(rConPage);
                reportsAmount = contentService.getAllReportsAmount().get();

            } else if (ownedFrameworks.size() > 0 || user.isVerify()) {
                List<Long> frameworkIds = new ArrayList<>();
                List<Long> frameworkIdsForReportedComments = new ArrayList<>();
                getVerifiedAndOwnedFrameworks(user, frameworkIds, frameworkIdsForReportedComments);

                contentList = contentService.getReportsByFrameworks(frameworkIds, rConPage);
                reportsAmount = contentService.getReportsAmount(frameworkIds).get();
            }

            if( contentList.size() > 0) {
                reportDTOList = contentList.stream().map(ReportDTO::fromReportContent).collect(Collectors.toList());
                double pages = Math.ceil(((double) reportsAmount) / PAGE_SIZE);
                Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOList){});
                return addPaginationLinks(response, "rConPage", rConPage, pages).build();
            }

            return Response.noContent().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    

    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response demoteMod (@PathParam("id") Long verificationId) {
        Optional<VerifyUser> vu = us.getVerifyById(verificationId);
        if( vu.isPresent() && !vu.get().isPending() ){
            us.deleteVerification(verificationId);
            LOGGER.info("User: Demoted user according to Verification {}", verificationId);
            return Response.ok().build();
        }
        LOGGER.error("User: Attempting to demote a non promoted user");
        return Response.notModified().build();
    }

    @POST
    @Path("/pending/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptMod (@PathParam("id") Long verificationId) {
        Optional<VerifyUser> vu= us.getVerifyById(verificationId);
        if(vu.isPresent()) {
            if (vu.get().isPending()) {
                us.verify(verificationId);
                Optional<User> user = us.findById(vu.get().getUserId());
                user.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
                return Response.ok().build();
            }
            return Response.notModified().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/pending/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response rejectMod (@PathParam("id") Long verificationId) {
        Optional<VerifyUser> vu= us.getVerifyById(verificationId);
        if(vu.isPresent() && vu.get().isPending()) {
            if(vu.get().isPending()) {
                us.deleteVerification(verificationId);
            }
            return Response.notModified().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/tech/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response applyForTech (@PathParam("id") Long techId) {
        Optional<User> user = us.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        if (user.isPresent()) {
            if (!user.get().hasAppliedToFramework(techId)) {
                Optional<Framework> framework = fs.findById(techId);
                if (framework.isPresent()) {
                    us.createVerify(user.get(), framework.get());
                    return Response.ok().build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/tech/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response quitModdingFromTech (@PathParam("id") Long techId) {
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){
            if (us.quitModdingFromTech(user.get(), techId)) {
                LOGGER.info("Tech {}: User {} is no longer a Mod", techId, user.get().getId());
                return Response.ok().build();
            }
            LOGGER.error("Tech {}: User {} tried to quit from being a Mod without previously being one", techId, user.get().getId());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOGGER.error("Tech {}: Unauthorized user tried to quit modding", techId);
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/reports/comment/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptCommentReport(@PathParam("id") Long commentId) {
        final Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                commentService.denyReport(commentId);
                LOGGER.info("User: Comment {} was removed from its report", commentId);
                return Response.ok().build();
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported comment", user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        LOGGER.error("User: Unauthorized user attempted to ignore a reported comment");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/reports/comment/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteCommentReport(@PathParam("id") Long commentId) {
        final Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                Optional<Comment> commentOptional = commentService.getById(commentId);
                if( commentOptional.isPresent() ){
                    commentService.acceptReport(commentId);
                    LOGGER.info("User: Comment {} was successfully deleted", commentId);
                    return Response.ok().build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported comment", user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("User: Unauthorized user attempted to ignore a reported comment");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/reports/content/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response acceptContentReport(@PathParam("id") Long contentId) {
        final Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                contentService.denyReport(contentId);
                LOGGER.info("User: Content {} was removed from its report", contentId);
                return Response.ok().build();
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported content", user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("User: Unauthorized user attempted to ignore a reported content");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/reports/content/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteContentReport(@PathParam("id") Long contentId) {
        final Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                Optional<Content> contentOptional = contentService.getById(contentId);
                if( contentOptional.isPresent() ){
                    contentService.acceptReport(contentId);
                    LOGGER.info("User: Content {} was successfully deleted", contentId);
                    return Response.ok().build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported content", user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("User: Unauthorized user attempted to ignore a reported content");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}