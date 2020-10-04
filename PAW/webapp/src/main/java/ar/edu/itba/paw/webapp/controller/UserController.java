package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/reject")
    public ModelAndView RejectCandidate(@RequestParam("id") final long verificationId) {
        Optional<VerifyUser> vu= us.getVerifyById(verificationId);
        if(vu.isPresent() && vu.get().isPending()) {
            us.deleteVerification(verificationId);
        }

        return modPage();
    }

    @RequestMapping("/accept")
    public ModelAndView AcceptCandidate(@RequestParam("id") final long verificationId) {
        Optional<VerifyUser> vu= us.getVerifyById(verificationId);
        if(vu.isPresent() && vu.get().isPending()) {
            us.verify(verificationId);
            Optional<User> user = us.findById(vu.get().getUserId());
            if(user.isPresent())
                us.modMailing(user.get(),vu.get().getFrameworkName());
        }
        return modPage();
    }

    @RequestMapping("/demote")
    public ModelAndView demote(@RequestParam("id") final long verificationId){
        Optional<VerifyUser> vu = us.getVerifyById(verificationId);
        if( vu.isPresent() && !vu.get().isPending() ){
            us.deleteVerification(verificationId);
        }

        return modPage();
    }

    @RequestMapping("/mod")
    public ModelAndView modPage(){
        ModelAndView mav = new ModelAndView("admin/mod_page");

        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = us.findByUsername(username);
        if( userOptional.isPresent()){
            User user = userOptional.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isAdmin",user.isAdmin());
            if( user.isAdmin()) {
                List<VerifyUser> mods = us.getVerifyByPending(false);
                List<VerifyUser> verify = us.getVerifyByPending(true);
                List<VerifyUser> applicants = new LinkedList<>();
                verify.forEach(verifyUser -> {
                    if(verifyUser.getComment() == null ){
                        applicants.add(verifyUser);
                    }
                });
                verify.removeAll(applicants);
                mav.addObject("mods",mods);
                mav.addObject("pendingToVerify", verify);
                mav.addObject("pendingApplicants", applicants);
                mav.addObject("reportedComments", commentService.getAllReport());
                mav.addObject("reportedContents", contentService.getAllReports());
                return mav;
            }
            else if( user.isVerify() ){
                List<VerifyUser> verify = new LinkedList<>();
                user.getVerifications().forEach(verifyUser -> {
                    verify.addAll(us.getVerifyByFramework(verifyUser.getFrameworkId(),true));
                });
                List<VerifyUser> applicants = new LinkedList<>();
                verify.forEach(verifyUser -> {
                    if(verifyUser.getComment() == null ){
                        applicants.add(verifyUser);
                    }
                });
                verify.removeAll(applicants);
                mav.addObject("pendingToVerify",verify);
                mav.addObject("pendingApplicants", applicants);
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

    @RequestMapping("/mod/comment/delete")
    public ModelAndView deleteComment(@RequestParam("commentId") final long commentId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Comment> commentOptional = commentService.getById(commentId);
                if( commentOptional.isPresent() ){
                    commentService.acceptReport(commentId);
                    return modPage();
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/mod/comment/ignore")
    public ModelAndView ignoreComment(@RequestParam("commentId") final long commentId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                commentService.denyReport(commentId);

                return modPage();
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/mod/content/delete")
    public ModelAndView deleteContent(@RequestParam("contentId") final long contentId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Content> contentOptional = contentService.getById(contentId);
                if( contentOptional.isPresent() ){
                    contentService.acceptReport(contentId);
                    return modPage();
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/mod/content/ignore")
    public ModelAndView ignoreContent(@RequestParam("contentId") final long contentId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if( user.isAdmin() ){
                Optional<Content> contentOptional = contentService.getById(contentId);
                if( contentOptional.isPresent() ){
                    contentService.denyReport(contentId);
                    return modPage();
                }
            }
        }

        return ErrorController.redirectToErrorView();
    }

}
