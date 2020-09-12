package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.GenericResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
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
import sun.misc.resources.Messages;

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

    @Autowired
    private EmailService es;

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
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, HttpServletRequest request, final BindingResult errors) {
        if (errors.hasErrors()) {
            return index(form);
        }
        try {
        User registered = us.create(form.getUsername(), form.getEmail(), form.getPassword());
        String appUrl = request.getContextPath();
        //eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        es.sendEmailConfirmation(registered,appUrl);
        } catch (UserAlreadyExistException uaeEx) {
                ModelAndView mav = new ModelAndView("session/registerForm");
                mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
                return mav;
        } catch (RuntimeException ex) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("errorMessage", "Something went wrong! Please, try again");
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
            return "redirect:/session/badUser.html?lang=" + locale.getLanguage();
        }
        User user =us.findById((int) verificationToken.getUserId());
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getexpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "The link has expired! Do you want to send another confirmation link?");
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "redirect:/session/badUser.html?lang=" + locale.getLanguage();
        }

        user.setEnable(true);
        us.saveRegisteredUser(user);
        //return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        return "redirect:/";
    }

    @RequestMapping(value= "/user/resendRegistrationToken", method = RequestMethod.GET)
    public GenericResponse resendRegistrationToken(
            HttpServletRequest request, @RequestParam("token") String existingToken) {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        VerificationToken newToken = us.generateNewVerificationToken(existingToken);

        User user = us.findById((int) newToken.getUserId());
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);
        mailSender.send(email);

        return new GenericResponse("Another mail was sent!");
    }
    private SimpleMailMessage constructResendVerificationTokenEmail
            (String contextPath, Locale locale, VerificationToken newToken, User user) {
        String confirmationUrl =
                contextPath + "/regitrationConfirm.html?token=" + newToken.getToken();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(" rn" + confirmationUrl);
        email.setFrom("support.email");
        email.setTo(user.getMail());
        return email;
    }
    /*@ModelAttribute("userId")
    public Integer loggedUser(final HttpSession session)
    {
        return (Integer) session.getAttribute();
    }*/

}
