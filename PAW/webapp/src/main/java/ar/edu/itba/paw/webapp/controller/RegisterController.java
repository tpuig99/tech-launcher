package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.GenericResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Controller
public class RegisterController {
    @Autowired
    private UserService us;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            return mav;
    }

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors,HttpServletRequest request) {
        if (errors.hasErrors()) {
            return index(form);
        }
        try {
        User registered = us.create(form.getUsername(), form.getEmail(), form.getPassword());
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl,false));
        } catch (UserAlreadyExistException uaeEx) {
                ModelAndView mav = new ModelAndView("session/registerForm");
                mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
                return mav;
        } catch (RuntimeException ex) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            System.out.println(ex.getMessage());
            mav.addObject("errorMessage", "Something went wrong! Please, try again. \n"+ex.getMessage());
            return mav;
        }
        return new ModelAndView("redirect:/register/success/1");
    }

    @RequestMapping("/register/success/1")
    public ModelAndView success1(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message","We sent you an email to verify your account. Here's a button to the Home page, so you can start browsing ");
        mav.addObject("title","Created account successfully!");
        return mav;
    }

    @RequestMapping("/register/success/2")
    public ModelAndView success2(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message","Yay!! You are now a user of Tech Launcher, our page! Press the Home button and start contributing to our community ");
        mav.addObject("title","Your email has been confirmed!");
        return mav;
    }

    @RequestMapping("/register/success/3")
    public ModelAndView success3(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message","We sent you an email to verify your account again. Here's a button to the Home page, so you can start browsing");
        mav.addObject("title","Verification email resent");
        return mav;
    }

    @RequestMapping(value="/regitrationConfirm", method = { RequestMethod.GET })
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        VerificationToken verificationToken = us.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("message", "Something went wrong! Do you want to send another confirmation link?");
            return "session/badUser";
        }
        Optional<User> user =us.findById((int) verificationToken.getUserId());

        if (user.isPresent()) {
            if(user.get().isEnable()){
                return "redirect:/register/success/2";
            }
            Calendar cal = Calendar.getInstance();
            if ((verificationToken.getexpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
                model.addAttribute("message", "The link has expired! Do you want to send another confirmation link?");
                model.addAttribute("expired", true);
                model.addAttribute("token", token);
                return "session/badUser";
            }

            user.get().setEnable(true);
            us.saveRegisteredUser(user.get());
            //return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
            return "redirect:/register/success/2";
        }

        return "redirect:/error";
    }

    @RequestMapping(value= "/register/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(
            HttpServletRequest request, @RequestParam("token") String existingToken) {

        VerificationToken verificationToken = us.getVerificationToken(existingToken);
        Optional<User> registered = us.findById((int) verificationToken.getUserId());
        String appUrl = request.getRequestURL().substring(0,request.getRequestURL().indexOf(request.getPathInfo())) ;

        if(registered.isPresent()) {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), request.getLocale(), appUrl,true));
            return "redirect:/register/success/3";
        }

        return "redirect:/error";
    }
    /*@ModelAttribute("userId")
    public Integer loggedUser(final HttpSession session)
    {
        return (Integer) session.getAttribute();
    }*/

}
