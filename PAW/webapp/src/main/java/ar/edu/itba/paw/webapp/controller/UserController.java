package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.mod_page.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
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

    @Autowired
    private MessageSource messageSource;

    private final long pageStart=1;
    private final long PAGE_SIZE=5;

    private static final String MOD_VIEW = "/mod?tabs=";

    public static ModelAndView redirectToModView(String currentTab) { return new ModelAndView("redirect:" + MOD_VIEW + currentTab); }

    @RequestMapping("/apply")
    public String AddCandidate(HttpServletRequest request, @RequestParam("id") final long frameworkId) {
        String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> user = us.findByUsername(username);
        if(user.isPresent()){
            if(!user.get().hasAppliedToFramework(frameworkId)){
                Optional<Framework> framework = fs.findById(frameworkId);
                if(framework.isPresent())
                    us.createVerify(user.get(),framework.get());
            }

        }
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @RequestMapping(path = {"/reject"}, method = RequestMethod.POST )
    public ModelAndView RejectCandidate(@ModelAttribute("rejectUserForm") final RejectUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getRejectUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.deleteVerification(form.getRejectUserVerificationId());
        }

        return redirectToModView(DEFAULT_PROMOTE_TAB);
    }

    @RequestMapping(path = {"/rejectPending"}, method = RequestMethod.POST )
    public ModelAndView RejectPendingCandidate(@ModelAttribute("rejectPendingUserForm") final RejectPendingUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getRejectPendingUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.deleteVerification(form.getRejectPendingUserVerificationId());
        }

        return redirectToModView(DEFAULT_PROMOTE_TAB);
    }

    @RequestMapping(path = {"/accept"}, method = RequestMethod.POST)
    public ModelAndView AcceptCandidate(@ModelAttribute("promoteUserForm") final PromoteUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getPromoteUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.verify(form.getPromoteUserVerificationId());
            Optional<User> user = us.findById(vu.get().getUserId());
            user.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
        }
        return redirectToModView(DEFAULT_PROMOTE_TAB);
    }

    @RequestMapping(path = {"/acceptPending"}, method = RequestMethod.POST)
    public ModelAndView AcceptPendingCandidate(@ModelAttribute("promoteUserForm") final PromotePendingUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getPromotePendingUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.verify(form.getPromotePendingUserVerificationId());
            Optional<User> user = us.findById(vu.get().getUserId());
            user.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
        }
        return redirectToModView(DEFAULT_PROMOTE_TAB);
    }

    @RequestMapping(path = {"/demote"}, method = RequestMethod.POST)
    public ModelAndView demote(@ModelAttribute("revokePromotionForm") final RevokePromotionForm form){
        Optional<VerifyUser> vu = us.getVerifyById(form.getRevokePromotionVerificationId());
        if( vu.isPresent() && !vu.get().isPending() ){
            us.deleteVerification(form.getRevokePromotionVerificationId());
            LOGGER.info("User: Demoted user according to Verification {}",form.getRevokePromotionVerificationId());
        }

        LOGGER.error("User: Attempting to demote a non promoted user");
        return redirectToModView(DEFAULT_DEMOTE_TAB);
    }

    @RequestMapping("/mod")
    public ModelAndView modPage(@RequestParam(value = "tabs", defaultValue = DEFAULT_PROMOTE_TAB, required = false) final String tabs,
                                @RequestParam(value = "modsPage", required = false) final Long modsPage,
                                @RequestParam(value = "rComPage", required = false) final Long rComPage,
                                @RequestParam(value = "applicantsPage", required = false) final Long applicantsPage,
                                @RequestParam(value = "verifyPage", required = false) final Long verifyPage,
                                @RequestParam(value = "rConPage", required = false) final Long rConPage){
        ModelAndView mav = new ModelAndView("admin/mod_page");

        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("ignoreContentForm", new IgnoreContentForm());
        mav.addObject("deleteContentForm", new DeleteContentForm());
        mav.addObject("ignoreCommentForm", new IgnoreCommentForm());
        mav.addObject("deleteCommentForm", new DeleteCommentForm());
        mav.addObject("rejectUserForm", new RejectUserForm());
        mav.addObject("promoteUserForm", new PromoteUserForm());
        mav.addObject("rejectPendingUserForm", new RejectPendingUserForm());
        mav.addObject("promotePendingUserForm", new PromotePendingUserForm());
        mav.addObject("revokePromotionForm", new RevokePromotionForm());
        mav.addObject("selectTab",tabs);
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isAdmin",user.isAdmin());
            mav.addObject("isOwner", !user.getOwnedFrameworks().isEmpty());
            List<Framework> ownedFrameworks = user.getOwnedFrameworks();
            if( user.isAdmin()) {
                List<VerifyUser> mods = us.getVerifyByPending(false, modsPage == null ? pageStart : modsPage);
                List<VerifyUser> verify = us.getVerifyByPending(true, verifyPage == null ? pageStart : verifyPage);
                List<VerifyUser> applicants = us.getApplicantsByPending(true, applicantsPage == null ? pageStart : applicantsPage);
                List<ReportComment> reportedComments = commentService.getAllReport(rComPage == null ? pageStart : rComPage);
                List<ReportContent> reportedContents = contentService.getAllReports(rConPage == null ? pageStart : rConPage);
                mav.addObject("pageSize", PAGE_SIZE);

                mav.addObject("mods", mods);
                mav.addObject("modsAmount", us.getVerifyByPendingAmount(false).get());
                mav.addObject("modsPage", modsPage == null ? pageStart : modsPage);

                mav.addObject("pendingToVerify", verify);
                mav.addObject("verifyAmount", us.getVerifyByPendingAmount(true).get());
                mav.addObject("verifyPage", verifyPage == null ? pageStart : verifyPage);

                mav.addObject("pendingApplicants", applicants);
                mav.addObject("applicantsAmount", us.getApplicantsByPendingAmount(true).get());
                mav.addObject("applicantsPage", applicantsPage == null ? pageStart : applicantsPage);

                mav.addObject("reportedComments", reportedComments);
                mav.addObject("reportedCommentsAmount", commentService.getAllReportsAmount().get());
                mav.addObject("rComPage", rComPage == null ? pageStart : rComPage);

                mav.addObject("reportedContents", reportedContents);
                mav.addObject("reportedContentAmount", contentService.getAllReportsAmount().get());
                mav.addObject("rConPage", rConPage == null ? pageStart : rConPage);

                return mav;
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

                List<VerifyUser> mods = us.getVerifyByPendingAndFrameworks(false, frameworksIdsForReportedComments, modsPage == null ? pageStart : modsPage);
                List<VerifyUser> verify = us.getVerifyByFrameworks(frameworksIds, true, verifyPage == null ? pageStart:verifyPage);
                List<VerifyUser> applicants = us.getApplicantsByFrameworks(frameworksIds, applicantsPage == null ? pageStart:applicantsPage);
                List<ReportContent> reportContents = contentService.getReportsByFrameworks(frameworksIds, rConPage == null ? pageStart:rConPage);
                List<ReportComment> reportedComments = commentService.getReportsByFrameworks( frameworksIdsForReportedComments, rComPage == null ? pageStart:rComPage);

                mav.addObject("pageSize", PAGE_SIZE);

                mav.addObject("mods", mods);
                mav.addObject("modsAmount", us.getVerifyByPendingAndFrameworksAmount(false, frameworksIdsForReportedComments)); // TODO: getVerifyByPendingAndFrameworkAmount
                mav.addObject("modsPage", modsPage == null ? pageStart : modsPage);

                mav.addObject("pendingToVerify",verify);
                mav.addObject("verifyAmount",us.getVerifyByFrameworkAmount(frameworksIds,true).get());
                mav.addObject("verifyPage", verifyPage == null ? pageStart:verifyPage);

                mav.addObject("pendingApplicants", applicants);
                mav.addObject("applicantsAmount",us.getApplicantsByFrameworkAmount(frameworksIds,true).get());
                mav.addObject("applicantsPage", applicantsPage == null ? pageStart:applicantsPage);

                mav.addObject("reportedComments", reportedComments);
                mav.addObject("reportedCommentsAmount", commentService.getReportsAmountByFrameworks(frameworksIdsForReportedComments)); // TODO: getVerifyByPendingAndFrameworkAmount
                mav.addObject("rComPage", rComPage == null ? pageStart : rComPage);

                mav.addObject("reportedContents", reportContents);
                mav.addObject("reportedContentAmount",contentService.getReportsAmount(frameworksIds).get());
                mav.addObject("rConPage", rConPage == null ? pageStart:rConPage);
                return mav;
            }
            else if( user.isVerify() ){
                List<Long> frameworksIds = new LinkedList<>();
                user.getVerifications().forEach( verifyUser -> {
                    if( !verifyUser.isPending() )
                        frameworksIds.add(verifyUser.getFrameworkId());
                });
                List<VerifyUser> verify = us.getVerifyByFrameworks(frameworksIds, true, verifyPage == null ? pageStart:verifyPage);
                List<VerifyUser> applicants = us.getApplicantsByFrameworks(frameworksIds, applicantsPage == null ? pageStart:applicantsPage);
                List<ReportContent> reportContents = contentService.getReportsByFrameworks(frameworksIds, rConPage == null ? pageStart:rConPage);

                mav.addObject("pageSize", PAGE_SIZE);

                mav.addObject("pendingToVerify",verify);
                mav.addObject("verifyAmount",us.getVerifyByFrameworkAmount(frameworksIds,true).get());
                mav.addObject("verifyPage", verifyPage == null ? pageStart:verifyPage);

                mav.addObject("pendingApplicants", applicants);
                mav.addObject("applicantsAmount",us.getApplicantsByFrameworkAmount(frameworksIds,true).get());
                mav.addObject("applicantsPage", applicantsPage == null ? pageStart:applicantsPage);

                mav.addObject("reportedContents", reportContents);
                mav.addObject("reportedContentAmount",contentService.getReportsAmount(frameworksIds).get());
                mav.addObject("rConPage", rConPage == null ? pageStart:rConPage);
                return mav;
            }

            LOGGER.error("User: User {} does not have enough privileges to access Mod Page", user.getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("User: Unauthorized user attempted to access mod page");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/mod/comment/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@ModelAttribute("deleteCommentForm") final DeleteCommentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                Optional<Comment> commentOptional = commentService.getById(form.getDeleteCommentId());
                if( commentOptional.isPresent() ){
                    commentService.acceptReport(form.getDeleteCommentId());
                    LOGGER.info("User: Comment {} was successfully deleted", form.getDeleteCommentId());
                    return redirectToModView(DEFAULT_REPORT_TAB);
                }
                LOGGER.error("User: Comment {} was not found", form.getDeleteCommentId());
                return ErrorController.redirectToErrorView();
            }
            LOGGER.error("User: User {} has not enough privileges to remove a reported comment", user.getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("User: Unauthorized user attempted to remove a reported comment");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/mod/comment/ignore"}, method = RequestMethod.POST)
    public ModelAndView ignoreComment(@ModelAttribute("ignoreCommentForm") final IgnoreCommentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.getOwnedFrameworks().size() > 0){
                commentService.denyReport(form.getIgnoreCommentId());
                LOGGER.info("User: Comment {} was removed from its report", form.getIgnoreCommentId());
                return redirectToModView(DEFAULT_REPORT_TAB);
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported comment", user.getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("User: Unauthorized user attempted to ignore a reported comment");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = {"/mod/content/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteContent(@ModelAttribute("deleteContentForm") final DeleteContentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.isVerify()){
                Optional<Content> contentOptional = contentService.getById(form.getDeleteContentId());
                if( contentOptional.isPresent() ){
                    contentService.acceptReport(form.getDeleteContentId());
                    LOGGER.info("User: Content {} was successfully deleted", form.getDeleteContentId());
                    return redirectToModView(DEFAULT_REPORT_TAB);
                }
                LOGGER.error("User: Content {} was not found", form.getDeleteContentId());
                return ErrorController.redirectToErrorView();
            }
            LOGGER.error("User: User {} has not enough privileges to delete a reported content", user.getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("User: Unauthorized user attempted to delete a reported content");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/mod/content/ignore"},method = RequestMethod.POST)
    public ModelAndView ignoreContent(@ModelAttribute("ignoreContentForm") final IgnoreContentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() || user.isVerify()){
                Optional<Content> contentOptional = contentService.getById(form.getIgnoreContentId());
                if( contentOptional.isPresent() ){
                    contentService.denyReport(form.getIgnoreContentId());
                    LOGGER.info("User: Content {} was removed from its report", form.getIgnoreContentId());
                    return redirectToModView(DEFAULT_REPORT_TAB);
                }
            }
            LOGGER.error("User: User {} has not enough privileges to ignore a reported content", user.getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("User: Unauthorized user attempted to ignore a reported content");
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping("/mod/quit")
    public ModelAndView QuitMod(@RequestParam("fId") final long fId,@RequestParam("category") final String cat) {
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(user.isPresent()){
            if (us.quitModdingFromTech(user.get(), fId)) {
                LOGGER.info("Tech {}: User {} is no longer a Mod", fId, user.get().getId());
                //return FrameworkController.redirectToFramework(fId, cat);
            }

            LOGGER.error("Tech {}: User {} tried to quit from being a Mod without previously being one", fId, user.get().getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Unauthorized user tried to quit modding", fId);
        return ErrorController.redirectToErrorView();
    }
}
