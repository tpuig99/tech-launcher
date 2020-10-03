package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.session.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.form.session.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class RegisterController {
    @Autowired
    private UserService us;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

//    @Autowired
//    LocaleResolver localeResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authManager;

    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
        ModelAndView mav = new ModelAndView("session/registerForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (us.findByUsername(username).isPresent()) {
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return index(form);
        }
        try {
            User registered = us.create(form.getUsername(), form.getEmail(), form.getPassword());
            // GET URL - GET URI + GETCONTEXTPATH
            String appUrl = request.getRequestURL().toString();
            appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl, false));
            internalLogin(form.getUsername(), form.getPassword(),request);
        } catch (UserAlreadyExistException uaeEx) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
            return mav;
        } catch (RuntimeException ex) {
            ModelAndView mav = new ModelAndView("session/registerForm");
            System.out.println(ex.getMessage());
            mav.addObject("errorMessage", messageSource.getMessage("register.error.try_again", new Object[]{}, Locale.getDefault()));
            return mav;
        }
        return new ModelAndView("redirect:/register/success/1");
    }

    @RequestMapping("/register/success/1")
    public ModelAndView success1() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.email.sent", new Object[]{}, Locale.getDefault()));
        mav.addObject("title", messageSource.getMessage("register.success.account.created", new Object[]{}, Locale.getDefault()));
        return mav;
    }

    @RequestMapping("/register/success/2")
    public ModelAndView success2() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.account.validated", new Object[]{}, Locale.getDefault()));
        mav.addObject("title", messageSource.getMessage("register.success.email.validated", new Object[]{}, Locale.getDefault()));
        return mav;
    }

    @RequestMapping("/register/success/3")
    public ModelAndView success3() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.email.resent", new Object[]{}, Locale.getDefault()));
        mav.addObject("title", messageSource.getMessage("register.success.account.resent", new Object[]{}, Locale.getDefault()));
        return mav;
    }

    @RequestMapping(value = "/registrationConfirm", method = {RequestMethod.GET})
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
                    model.addAttribute("message", messageSource.getMessage("register.error.link_expired", new Object[]{}, Locale.getDefault()));
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
        model.addAttribute("message", messageSource.getMessage("register.error.resend", new Object[]{}, Locale.getDefault()));
        return "session/badUser";
    }

    @RequestMapping(value = "/register/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(
            HttpServletRequest request, @RequestParam("token") String existingToken) {

        Optional<VerificationToken> verificationToken = us.getVerificationToken(existingToken);

        if (verificationToken.isPresent()) {
            Optional<User> registered = us.findById((int) verificationToken.get().getUserId());
            String appUrl = request.getRequestURL().toString();
            appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());
            if (registered.isPresent()) {
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), request.getLocale(), appUrl, true));
                return "redirect:/register/success/3";
            }
        }

        return "redirect:/error";
    }

    @RequestMapping("/chgpassword")
    public ModelAndView passwordChange(@ModelAttribute("passwordForm") final PasswordForm form, @RequestParam(value = "id", required = false) Long userId) {
        ModelAndView mav = new ModelAndView("session/passwordForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = us.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("user_id", user.getId());
        } else if (userId != null) {
            mav.addObject("user_id", userId);
        } else {
            return ErrorController.redirectToErrorView();
        }
        return mav;
    }

    @RequestMapping(value = "/updpassword", method = {RequestMethod.POST})
    public ModelAndView updpassword(@Valid @ModelAttribute("passwordForm") final PasswordForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return passwordChange(form, form.getUserId());
        }
        us.updatePassword(form.getUserId(), form.getPassword());
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            Optional<User> optionalUser = us.findById(form.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                internalLogin(user.getUsername(), form.getPassword(), request);
            }
        }
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.changed_password.description", new Object[]{}, Locale.getDefault()));
        mav.addObject("title", messageSource.getMessage("register.changed_password.title", new Object[]{}, Locale.getDefault()));
        return mav;
    }

    private void internalLogin(String user, String pass, HttpServletRequest req) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, pass);
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    @RequestMapping(value = "/forgetpassword")
    public ModelAndView forgetPassword(@ModelAttribute("ForgetPassForm") final ForgetPassForm form) {
        ModelAndView mav = new ModelAndView("session/recoveryPassForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (us.findByUsername(username).isPresent()) {
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }

    @RequestMapping(value = "/recoveringToken")
    public ModelAndView recoverToken(@RequestParam("token") String token) {
        String[] strings = token.split("-a_d-ss-");
        Long userId = Long.valueOf(strings[strings.length - 1]);
        return new ModelAndView("redirect:/chgpassword?id=" + userId);
    }

    @RequestMapping(value = "/recoverpassword", method = RequestMethod.POST)
    public ModelAndView recoverPassword(@Valid @ModelAttribute("ForgetPassForm") final ForgetPassForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return forgetPassword(form);
        }
        Optional<User> optionalUser = us.findByMail(form.getEmail());
        if (!optionalUser.isPresent()) {
            ModelAndView mav = new ModelAndView("session/recoveryPassForm");
            mav.addObject("errorMessage", messageSource.getMessage("register.error.wrong_email", new Object[]{}, Locale.getDefault()));
            return mav;
        }
        User user = optionalUser.get();
        String appUrl = request.getRequestURL().toString();
        appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());

        us.passwordMailing(user, appUrl);
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.change_password.message", new Object[]{}, Locale.getDefault()));
        mav.addObject("title", messageSource.getMessage("register.change_password.title", new Object[]{}, Locale.getDefault()));
        return mav;
    }
}

