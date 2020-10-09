package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.mod_page.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserService us;
    @Autowired
    FrameworkService fs;

    @Autowired
    CommentService commentService;

    @Autowired
    ContentService contentService;
    @Autowired
    MessageSource messageSource;

    private long pageStart=1;
    private final long PAGESIZE=5;

    @RequestMapping("/apply")
    public String AddCandidate(HttpServletRequest request, @RequestParam("id") final long frameworkId) {
        String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> user = us.findByUsername(username);
        if(user.isPresent()){
            Optional<VerifyUser> vu = us.getVerifyByFrameworkAndUser(frameworkId,user.get().getId());
            if(!vu.isPresent())
                us.createVerify(user.get().getId(),frameworkId);
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

        return modPage(null, null, null, null, null);
    }

    @RequestMapping(path = {"/rejectPending"}, method = RequestMethod.POST )
    public ModelAndView RejectPendingCandidate(@ModelAttribute("rejectPendingUserForm") final RejectPendingUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getRejectPendingUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.deleteVerification(form.getRejectPendingUserVerificationId());
        }

        return modPage(null, null, null, null, null);
    }

    @RequestMapping(path = {"/accept"}, method = RequestMethod.POST)
    public ModelAndView AcceptCandidate(@ModelAttribute("promoteUserForm") final PromoteUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getPromoteUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.verify(form.getPromoteUserVerificationId());
            Optional<User> user = us.findById(vu.get().getUserId());
            user.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
            }
        return modPage(null, null, null, null, null);
    }

    @RequestMapping(path = {"/acceptPending"}, method = RequestMethod.POST)
    public ModelAndView AcceptPendingCandidate(@ModelAttribute("promoteUserForm") final PromotePendingUserForm form) {
        Optional<VerifyUser> vu= us.getVerifyById(form.getPromotePendingUserVerificationId());
        if(vu.isPresent() && vu.get().isPending()) {
            us.verify(form.getPromotePendingUserVerificationId());
            Optional<User> user = us.findById(vu.get().getUserId());
            user.ifPresent(value -> us.modMailing(value, vu.get().getFrameworkName()));
        }
        return modPage(null, null, null, null, null);
    }

    @RequestMapping(path = {"/demote"}, method = RequestMethod.POST)
    public ModelAndView demote(@ModelAttribute("revokePromotionForm") final RevokePromotionForm form){
        Optional<VerifyUser> vu = us.getVerifyById(form.getRevokePromotionVerificationId());
        if( vu.isPresent() && !vu.get().isPending() ){
            us.deleteVerification(form.getRevokePromotionVerificationId());
        }

        return modPage(null, null, null, null, null);
    }

    @RequestMapping("/mod")
    public ModelAndView modPage(@RequestParam(value = "modsPage", required = false) final Long modsPage,
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


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = us.findByUsername(username);
        if( userOptional.isPresent()){
            User user = userOptional.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isAdmin",user.isAdmin());
            if( user.isAdmin()) {
                List<VerifyUser> mods = us.getVerifyByPending(false, modsPage == null ? pageStart:modsPage);
                List<VerifyUser> verify = us.getVerifyByPending(true, verifyPage == null ? pageStart:verifyPage);
                List<VerifyUser> applicants = us.getApplicantsByPending(true, applicantsPage == null ? pageStart:applicantsPage);
                List<ReportComment> reportedComments = commentService.getAllReport(rComPage == null ? pageStart:rComPage);
                List<ReportContent> reportedContents = contentService.getAllReports(rConPage == null ? pageStart:rConPage);
                mav.addObject("pageSize", PAGESIZE);

                mav.addObject("mods",mods);
                mav.addObject("modsPage", modsPage == null ? pageStart:modsPage);

                mav.addObject("pendingToVerify", verify);
                mav.addObject("verifyPage", verifyPage == null ? pageStart:verifyPage);

                mav.addObject("pendingApplicants", applicants);
                mav.addObject("applicantsPage", applicantsPage == null ? pageStart:applicantsPage);

                mav.addObject("reportedComments", reportedComments);
                mav.addObject("rComPage", rComPage == null ? pageStart:rComPage);

                mav.addObject("reportedContents", reportedContents );
                mav.addObject("rConPage", rConPage == null ? pageStart:rConPage);

                return mav;
            }
            else if( user.isVerify() ){
                List<Long> frameworksIds = new LinkedList<>();
                user.getVerifications().forEach( verifyUser -> {
                    frameworksIds.add(verifyUser.getFrameworkId());
                });
                List<VerifyUser> verify = us.getVerifyByFrameworks(frameworksIds, true, verifyPage == null ? pageStart:verifyPage);
                List<VerifyUser> applicants = us.getApplicantsByFrameworks(frameworksIds, applicantsPage == null ? pageStart:applicantsPage);
                List<ReportContent> reportContents = contentService.getReportsByFrameworks(frameworksIds, rConPage == null ? pageStart:rConPage);

                mav.addObject("pageSize", PAGESIZE);

                mav.addObject("pendingToVerify",verify);
                mav.addObject("verifyPage", verifyPage == null ? pageStart:verifyPage);

                mav.addObject("pendingApplicants", applicants);
                mav.addObject("applicantsPage", applicantsPage == null ? pageStart:applicantsPage);

                mav.addObject("reportedContents", reportContents);
                mav.addObject("rConPage", rConPage == null ? pageStart:rConPage);
                return mav;
            }
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping("/dismiss")
    public ModelAndView DismissMod(@RequestParam("id") final long verificationId) {
        //check the mav you want
        final ModelAndView mav = new ModelAndView("admin/mod_page");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = us.findByUsername(username);
        if( userOptional.isPresent()){
            User user = userOptional.get();
            if(user.isAdmin()) {
                Optional<VerifyUser> vu = us.getVerifyById(verificationId);
                if (vu.isPresent()) {
                    us.deleteVerification(verificationId);
                }
                return mav;
            }
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping("/quit")
    public ModelAndView QuitMod(@RequestParam("id") final long verificationId) {
        //check the mav you want
        final ModelAndView mav = new ModelAndView("admin/mod_page");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = us.findByUsername(username);
        if( userOptional.isPresent()){
            User user = userOptional.get();
            Optional<VerifyUser> vu = us.getVerifyById(verificationId);
            if (vu.isPresent()) {
                VerifyUser verifyUser = vu.get();
                if(verifyUser.getUserId()!=user.getId()){
                    //raise error --> only owner
                    return ErrorController.redirectToErrorView();
                }
                us.deleteVerification(verificationId);
            }
            return mav;
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/mod/comment/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@ModelAttribute("deleteCommentForm") final DeleteCommentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Comment> commentOptional = commentService.getById(form.getDeleteCommentId());
                if( commentOptional.isPresent() ){
                    commentService.acceptReport(form.getDeleteCommentId());
                    return modPage(null, null, null, null, null);
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/mod/comment/ignore"}, method = RequestMethod.POST)
    public ModelAndView ignoreComment(@ModelAttribute("ignoreCommentForm") final IgnoreCommentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                commentService.denyReport(form.getIgnoreCommentId());

                return modPage(null, null, null, null, null);
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping( path = {"/mod/content/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteContent(@ModelAttribute("deleteContentForm") final DeleteContentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Content> contentOptional = contentService.getById(form.getDeleteContentId());
                if( contentOptional.isPresent() ){
                    contentService.acceptReport(form.getDeleteContentId());
                    return modPage(null, null, null, null, null);
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/mod/content/ignore"},method = RequestMethod.POST)
    public ModelAndView ignoreContent(@ModelAttribute("ignoreContentForm") final IgnoreContentForm form){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Content> contentOptional = contentService.getById(form.getIgnoreContentId());
                if( contentOptional.isPresent() ){
                    contentService.denyReport(form.getIgnoreContentId());
                    return modPage(null, null, null, null, null);
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

}
