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

@Controller
public class HomeController {
    @Autowired
    private UserService us;

    @Autowired
    private FrameworkService fs;

    @Autowired
    ApplicationEventPublisher eventPublisher;


    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("frameworksList", fs.getAll() );
        return mav;
    }
    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
            return new ModelAndView("session/registerForm");
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
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/regitrationConfirm", method = { RequestMethod.GET })
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        VerificationToken verificationToken = us.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("message", "Something went wrong! Do you want to send another confirmation link?");
            return "session/badUser";
        }
        User user =us.findById((int) verificationToken.getUserId());
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getexpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "The link has expired! Do you want to send another confirmation link?");
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "session/badUser";
        }

        user.setEnable(true);
        us.saveRegisteredUser(user);
        //return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        return "redirect:/";
    }

    @RequestMapping(value= "/user/resendRegistrationToken", method = RequestMethod.GET)
    public GenericResponse resendRegistrationToken(
            HttpServletRequest request, @RequestParam("token") String existingToken) {

        VerificationToken verificationToken = us.getVerificationToken(existingToken);
        User registered = us.findById((int) verificationToken.getUserId());
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl,true));

        return new GenericResponse("Another mail was sent!");
    }
    /*@ModelAttribute("userId")
    public Integer loggedUser(final HttpSession session)
    {
        return (Integer) session.getAttribute();
    }*/

}
