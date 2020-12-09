package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.ModDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response modPage(@QueryParam("tabs") @DefaultValue(DEFAULT_PROMOTE_TAB) String tabs,
                            @QueryParam("modsPage") @DefaultValue(START_PAGE) Long modsPage,
                            @QueryParam("rComPage") @DefaultValue(START_PAGE) Long rComPage,
                            @QueryParam("applicantsPage") @DefaultValue(START_PAGE) Long applicantsPage,
                            @QueryParam("verifyPage") @DefaultValue(START_PAGE) Long verifyPage,
                            @QueryParam("rConPage") @DefaultValue(START_PAGE) Long rConPage) {

        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Integer modsAmount, verifyAmount, applicantsAmount, reportedCommentsAmount, reportedContentAmount;
        ModDTO modDTO = new ModDTO();

        if( userOptional.isPresent()){
            User user = userOptional.get();
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            if( user.isAdmin()) {
                modDTO.setMods(us.getVerifyByPending(false, modsPage == null ? pageStart : modsPage));
                modDTO.setVerified(us.getVerifyByPending(true, verifyPage == null ? pageStart : verifyPage));
                modDTO.setApplicants(us.getApplicantsByPending(true, applicantsPage == null ? pageStart : applicantsPage));
                modDTO.setReportedComments(commentService.getAllReport(rComPage == null ? pageStart : rComPage));
                modDTO.setReportedContents(contentService.getAllReports(rConPage == null ? pageStart : rConPage));

                modsAmount = us.getVerifyByPendingAmount(false).get();
                verifyAmount = us.getVerifyByPendingAmount(true).get();
                applicantsAmount = us.getApplicantsByPendingAmount(true).get();
                reportedCommentsAmount = commentService.getAllReportsAmount().get();
                reportedContentAmount = contentService.getAllReportsAmount().get();

                return Response.ok().build();

            } else if( ownedFrameworks.size() > 0){
                List<Long> frameworksIds = new LinkedList<>();
                user.getVerifications().forEach( verifyUser -> {
                    if( !verifyUser.isPending() )
                        frameworksIds.add(verifyUser.getFrameworkId());
                });
                List<Long> frameworksIdsForReportedComments = new LinkedList<>();
                ownedFrameworks.forEach( framework -> {
                    frameworksIds.add(framework.getId());
                    frameworksIdsForReportedComments.add(framework.getId());
                });

                modDTO.setMods(us.getVerifyByPendingAndFrameworks(false, frameworksIdsForReportedComments, modsPage == null ? pageStart : modsPage));
                modDTO.setVerified(us.getVerifyByFrameworks(frameworksIds, true, verifyPage == null ? pageStart:verifyPage));
                modDTO.setApplicants(us.getApplicantsByFrameworks(frameworksIds, applicantsPage == null ? pageStart:applicantsPage));
                modDTO.setReportedContents(contentService.getReportsByFrameworks(frameworksIds, rConPage == null ? pageStart:rConPage));
                modDTO.setReportedComments(commentService.getReportsByFrameworks( frameworksIdsForReportedComments, rComPage == null ? pageStart:rComPage));

                modsAmount = us.getVerifyByPendingAndFrameworksAmount(false, frameworksIdsForReportedComments); // TODO: getVerifyByPendingAndFrameworkAmount
                verifyAmount = us.getVerifyByFrameworkAmount(frameworksIds,true).get();
                applicantsAmount = us.getApplicantsByFrameworkAmount(frameworksIds,true).get();
                reportedCommentsAmount = commentService.getReportsAmountByFrameworks(frameworksIdsForReportedComments); // TODO: getVerifyByPendingAndFrameworkAmount
                reportedContentAmount = contentService.getReportsAmount(frameworksIds).get();

                return Response.ok().build();
            }
            else if( user.isVerify() ){
                List<Long> frameworksIds = new LinkedList<>();
                user.getVerifications().forEach( verifyUser -> {
                    if( !verifyUser.isPending() )
                        frameworksIds.add(verifyUser.getFrameworkId());
                });

                modDTO.setVerified(us.getVerifyByFrameworks(frameworksIds, true, verifyPage == null ? pageStart:verifyPage));
                modDTO.setApplicants(us.getApplicantsByFrameworks(frameworksIds, applicantsPage == null ? pageStart:applicantsPage));
                modDTO.setReportedContents(contentService.getReportsByFrameworks(frameworksIds, rConPage == null ? pageStart:rConPage));

                verifyAmount = us.getVerifyByFrameworkAmount(frameworksIds,true).get();
                applicantsAmount = us.getApplicantsByFrameworkAmount(frameworksIds,true).get();
                reportedContentAmount = contentService.getReportsAmount(frameworksIds).get();

                return Response.ok().build();
            }

            LOGGER.error("User: User {} does not have enough privileges to access Mod Page", user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.error("User: Unauthorized user attempted to access mod page");
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
