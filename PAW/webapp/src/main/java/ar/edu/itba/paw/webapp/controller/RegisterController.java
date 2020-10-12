package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.register.ForgetPassForm;
import ar.edu.itba.paw.webapp.form.register.OnRegistrationCompleteEvent;
import ar.edu.itba.paw.webapp.form.register.PasswordForm;
import ar.edu.itba.paw.webapp.form.register.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService us;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuthenticationManager authManager;

    @RequestMapping("/register")
    public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
        ModelAndView mav = new ModelAndView("session/registerForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

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
            LOGGER.info("Register: User '{}' registered successfully with id {}", registered.getUsername(),registered.getId());

        } catch (UserAlreadyExistException uaeEx) {
            LOGGER.error("Register: {}", UserAlreadyExistException.class);
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
            return mav;

        } catch (RuntimeException ex) {
            LOGGER.error("Register: {}", RuntimeException.class);
            ModelAndView mav = new ModelAndView("session/registerForm");
            mav.addObject("errorMessage", messageSource.getMessage("register.error.try_again", new Object[]{}, LocaleContextHolder.getLocale()));
            return mav;
        }

        return new ModelAndView("redirect:/register/success/1");
    }

    @RequestMapping("/register/success/1")
    public ModelAndView success1() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.email.sent", new Object[]{}, LocaleContextHolder.getLocale()));
        mav.addObject("title", messageSource.getMessage("register.success.account.created", new Object[]{}, LocaleContextHolder.getLocale()));
        return mav;
    }

    @RequestMapping("/register/success/2")
    public ModelAndView success2() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.account.validated", new Object[]{}, LocaleContextHolder.getLocale()));
        mav.addObject("title", messageSource.getMessage("register.success.email.validated", new Object[]{}, LocaleContextHolder.getLocale()));
        return mav;
    }

    @RequestMapping("/register/success/3")
    public ModelAndView success3() {
        ModelAndView mav = new ModelAndView("session/successful");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.success.email.resent", new Object[]{}, LocaleContextHolder.getLocale()));
        mav.addObject("title", messageSource.getMessage("register.success.account.resent", new Object[]{}, LocaleContextHolder.getLocale()));
        return mav;
    }

    @RequestMapping(value = "/registrationConfirm", method = {RequestMethod.GET})
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = us.getVerificationToken(token);
        LOGGER.info("Register: Confirming registration");
        if (verificationToken.isPresent()) {
            Optional<User> user = us.findById((int) verificationToken.get().getUserId());

            if (user.isPresent()) {
                if (user.get().isEnable()) {
                    LOGGER.info("Register: User {} was already enabled", user.get().getId());
                    return "redirect:/register/success/2";
                }
                Calendar cal = Calendar.getInstance();
                if ((verificationToken.get().getexpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
                    LOGGER.error("Register: Verification token for user {} expired", user.get().getId());
                    model.addAttribute("message", messageSource.getMessage("register.error.link_expired", new Object[]{}, LocaleContextHolder.getLocale()));
                    model.addAttribute("expired", true);
                    model.addAttribute("token", token);
                    return "session/badUser";
                }

                user.get().setEnable(true);
                us.saveRegisteredUser(user.get());
                LOGGER.info("Register: User {} is now verified", user.get().getId());
                return "redirect:/register/success/2";
            }

            LOGGER.error("Register: User {} does not exist", verificationToken.get().getUserId());
            return "redirect:/error";
        }

        LOGGER.error("Register: There was a problem with the verification token");
        model.addAttribute("message", messageSource.getMessage("register.error.resend", new Object[]{}, LocaleContextHolder.getLocale()));
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
                LOGGER.info("Register: Resending verification token for user {}", registered.get().getId());
                return "redirect:/register/success/3";
            }

            LOGGER.error("Register: User {} does not exist", verificationToken.get().getUserId());
            return "redirect:/error";
        }

        LOGGER.error("Register: There was a problem with the verification token");
        return "redirect:/error";
    }

    @RequestMapping("/chgpassword")
    public ModelAndView passwordChange(@ModelAttribute("passwordForm") final PasswordForm form, @RequestParam(value = "id", required = false) Long userId) {
        ModelAndView mav = new ModelAndView("session/passwordForm");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.isPresent()) {
            mav.addObject("user_isMod", user.get().isVerify() || user.get().isAdmin());
            mav.addObject("user_id", user.get().getId());
        } else if (userId != null) {
            mav.addObject("user_id", userId);
        } else {
            LOGGER.error("Register: Unauthorized user attempted to change password");
            return ErrorController.redirectToErrorView();
        }
        return mav;
    }

    @RequestMapping(value = "/updpassword", method = {RequestMethod.POST})
    public ModelAndView updpassword(@Valid @ModelAttribute("passwordForm") final PasswordForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            LOGGER.info("Register: Password Form for updating has errors");
            return passwordChange(form, form.getUserId());
        }
        us.updatePassword(form.getUserId(), form.getPassword());
        LOGGER.info("Register: User {} updated its password successfully", form.getUserId());

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            Optional<User> optionalUser = us.findById(form.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                internalLogin(user.getUsername(), form.getPassword(), request);
            }
        }
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.changed_password.description", new Object[]{}, LocaleContextHolder.getLocale()));
        mav.addObject("title", messageSource.getMessage("register.changed_password.title", new Object[]{}, LocaleContextHolder.getLocale()));
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

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

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
            LOGGER.info("Register: Password Form for recovery has errors");
            return forgetPassword(form);
        }
        Optional<User> optionalUser = us.findByMail(form.getEmail());
        if (!optionalUser.isPresent()) {
            LOGGER.error("Register: No user found with mail {}", form.getEmail());
            ModelAndView mav = new ModelAndView("session/recoveryPassForm");
            mav.addObject("errorMessage", messageSource.getMessage("register.error.wrong_email", new Object[]{}, LocaleContextHolder.getLocale()));
            return mav;
        }
        User user = optionalUser.get();
        String appUrl = request.getRequestURL().toString();
        appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());

        us.passwordMailing(user, appUrl);
        LOGGER.info("Register: Successfully sent recovery mail to {}", form.getEmail());
        ModelAndView mav = new ModelAndView("/session/successful_cngPassw");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("message", messageSource.getMessage("register.change_password.message", new Object[]{}, LocaleContextHolder.getLocale()));
        mav.addObject("title", messageSource.getMessage("register.change_password.title", new Object[]{}, LocaleContextHolder.getLocale()));
        return mav;
    }
}

