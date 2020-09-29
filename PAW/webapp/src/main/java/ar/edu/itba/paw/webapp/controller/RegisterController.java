package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.ForgetPassForm;
import ar.edu.itba.paw.webapp.form.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RegisterController {
    @Autowired
    private UserService us;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if( us.findByUsername(username).isPresent()){
                User user = us.findByUsername(username).get();
                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            }
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
            mav.addObject("errorMessage", messageSource.getMessage("register.error.try_again",new Object[]{},Locale.getDefault()));
            return mav;
        }
        return new ModelAndView("redirect:/register/success/1");
    }

    @RequestMapping("/register/success/1")
    public ModelAndView success1(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message",messageSource.getMessage("register.success.email.sent",new Object[]{},Locale.getDefault()));
        mav.addObject("title",messageSource.getMessage("register.success.account.created",new Object[]{},Locale.getDefault()));
        return mav;
    }

    @RequestMapping("/register/success/2")
    public ModelAndView success2(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message",messageSource.getMessage("register.success.account.validated",new Object[]{},Locale.getDefault()));
        mav.addObject("title",messageSource.getMessage("register.success.email.validated",new Object[]{},Locale.getDefault()));
        return mav;
    }

    @RequestMapping("/register/success/3")
    public ModelAndView success3(){
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message",messageSource.getMessage("register.success.email.resent",new Object[]{},Locale.getDefault()));
        mav.addObject("title",messageSource.getMessage("register.success.account.resent",new Object[]{},Locale.getDefault()));
        return mav;
    }

    @RequestMapping(value="/registrationConfirm", method = { RequestMethod.GET })
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        Optional<VerificationToken> verificationToken = us.getVerificationToken(token);

        if (verificationToken.isPresent()) {
            Optional<User> user = us.findById((int) verificationToken.get().getUserId());

            if (user.isPresent()) {
                if (user.get().isEnable()) {
                    return "redirect:/register/success/2";
                }
                Calendar cal = Calendar.getInstance();
                if ((verificationToken.get().getexpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
                    model.addAttribute("message", messageSource.getMessage("register.error.link_expired",new Object[]{},Locale.getDefault()));
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
        model.addAttribute("message", messageSource.getMessage("register.error.resend",new Object[]{},Locale.getDefault()));
        return "session/badUser";
    }

    @RequestMapping(value= "/register/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(
            HttpServletRequest request, @RequestParam("token") String existingToken) {

        Optional<VerificationToken> verificationToken = us.getVerificationToken(existingToken);

        if (verificationToken.isPresent()) {
            Optional<User> registered = us.findById((int) verificationToken.get().getUserId());
            String appUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getPathInfo()));

            if (registered.isPresent()) {
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), request.getLocale(), appUrl, true));
                return "redirect:/register/success/3";
            }
        }

        return "redirect:/error";
    }

    @RequestMapping("/chgpassword")
    public ModelAndView passwordChange(@ModelAttribute("passwordForm") final PasswordForm form,@RequestParam(value = "id",required = false) Long userId) {
        ModelAndView mav = new ModelAndView("session/passwordForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = us.findByUsername(username);
        if( optionalUser.isPresent()){
            User user = optionalUser.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("user_id",user.getId());
        }else if(userId!=null){
            mav.addObject("user_id",userId);
        } else{
            return ErrorController.redirectToErrorView();
        }
        return mav;
    }

    @RequestMapping(value = "/updpassword", method = { RequestMethod.POST })
    public ModelAndView updpassword(@Valid @ModelAttribute("passwordForm") final PasswordForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return passwordChange(form, form.getUserId());
        }
        us.updatePassword(form.getUserId(), form.getPassword());
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message",messageSource.getMessage("register.changed_password.description",new Object[]{},Locale.getDefault()));
        mav.addObject("title",messageSource.getMessage("register.changed_password.title",new Object[]{},Locale.getDefault()));
        return mav;
    }
    @RequestMapping(value = "/forgetpassword")
    public ModelAndView forgetPassword(@ModelAttribute("ForgetPassForm") final ForgetPassForm form) {
        ModelAndView mav = new ModelAndView("session/recoveryPassForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }

    @RequestMapping(value = "/recoveringToken")
    public ModelAndView recoverToken(@RequestParam("token") String token){
        String[] strings = token.split("--_-ss-");
        Long userId = Long.valueOf(strings[strings.length-1]);
        return new ModelAndView("redirect:/chgpassword?id="+userId);
    }

    @RequestMapping(value= "/recoverpassword", method = RequestMethod.POST)
    public ModelAndView recoverPassword(@Valid @ModelAttribute("ForgetPassForm") final ForgetPassForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return forgetPassword(form);
        }
        Optional<User> optionalUser = us.findByMail(form.getEmail());
        if(!optionalUser.isPresent()){
            ModelAndView mav = new ModelAndView("session/recoveryPassForm");
            mav.addObject("errorMessage", messageSource.getMessage("register.error.wrong_email",new Object[]{},Locale.getDefault()));
            return mav;
        }
        User user = optionalUser.get();
        SendEmail(user);
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message",messageSource.getMessage("register.change_password.message",new Object[]{},Locale.getDefault()));
        mav.addObject("title",messageSource.getMessage("register.change_password.title",new Object[]{},Locale.getDefault()));
        return mav;
    }
    void SendEmail(User user){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailSender.setJavaMailProperties(prop);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("confirmemailonly", "pawserver");
            }
        });
        mailSender.setSession(session);
        String recipientAddress = user.getMail();

        String token = UUID.randomUUID().toString()+"--_-ss-"+user.getId();
        String confirmationUrl = "/recoveringToken?token=" + token;

        String subject = messageSource.getMessage("email.recovery.subject",new Object[]{}, Locale.getDefault());
        String message = messageSource.getMessage("email.recovery.body",new Object[]{}, Locale.getDefault());


        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("confirmemailonly@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        //email.setText(message + "\r\n" + "http://pawserver.it.itba.edu.ar" + confirmationUrl);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
