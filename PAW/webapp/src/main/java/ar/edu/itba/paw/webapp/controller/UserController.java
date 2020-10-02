package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
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
                return mav;
            }
            else if( user.isVerify() ){
                List<VerifyUser> verify = new LinkedList<>();
                user.getVerifications().forEach(verifyUser -> {
                    verify.addAll(us.getVerifyByUser(verifyUser.getFrameworkId(),true));
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
}
