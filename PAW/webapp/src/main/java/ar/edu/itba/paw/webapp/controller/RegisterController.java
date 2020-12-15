package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserAddDTO;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Calendar;
import java.util.Optional;

@Path("register")
@Component
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService us;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response register (final UserAddDTO userDTO) {
        try {
            User registeredUser = us.create(userDTO.getUsername(), userDTO.getMail(), userDTO.getPassword());
            /* Add JWT */
            LOGGER.info("Register: User '{}' registered successfully with id {}", registeredUser.getUsername(), registeredUser.getId());
            UserDTO dto = UserDTO.fromUser(registeredUser,uriInfo);
            return Response.ok(dto).build();
        }
        catch (UserAlreadyExistException uaeEx) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



//    @RequestMapping(value = "/create", method = {RequestMethod.POST})
//    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
//        if (errors.hasErrors()) {
//            return index(form);
//        }
//        try {
//            User registered = us.create(form.getUsername(), form.getEmail(), form.getPassword());
//            // GET URL - GET URI + GETCONTEXTPATH
//            String appUrl = request.getRequestURL().toString();
//            appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());
//            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl, false));
//            us.internalLogin(form.getUsername(), form.getPassword(),request);
//            LOGGER.info("Register: User '{}' registered successfully with id {}", registered.getUsername(),registered.getId());
//
//        } catch (UserAlreadyExistException uaeEx) {
//            LOGGER.error("Register: {}", UserAlreadyExistException.class);
//            ModelAndView mav = new ModelAndView("session/registerForm");
//            mav.addObject("errorMessage", uaeEx.getLocalizedMessage());
//            return mav;
//
//        } catch (RuntimeException ex) {
//            LOGGER.error("Register: {}", RuntimeException.class);
//            ModelAndView mav = new ModelAndView("session/registerForm");
//            mav.addObject("errorMessage", messageSource.getMessage("register.error.try_again", new Object[]{}, LocaleContextHolder.getLocale()));
//            return mav;
//        }
//
//        return new ModelAndView("redirect:/register/success/pending_validation");
//    }

    @GET
    @Path("/confirm")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response confirmRegistration(@QueryParam("token") String token) {
        Optional<VerificationToken> verificationToken = us.getVerificationToken(token);

        LOGGER.info("Register: Confirming registration");
        if (verificationToken.isPresent()) {
            Optional<User> user = us.findById((int) verificationToken.get().getUserId());

            if (user.isPresent()) {
                if (user.get().isEnable()) {
                    LOGGER.info("Register: User {} was already enabled", user.get().getId());
                    return Response.ok().build();
                }
                Calendar cal = Calendar.getInstance();

                if ((verificationToken.get().getExpiryDay().getTime() - cal.getTime().getTime()) <= 0) {
                    LOGGER.error("Register: Verification token for user {} expired", user.get().getId());
//                    model.addAttribute("message", messageSource.getMessage("register.error.link_expired", new Object[]{}, LocaleContextHolder.getLocale()));
//                    model.addAttribute("expired", true);
//                    model.addAttribute("token", token);
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                user.get().setEnable(true);
                us.saveRegisteredUser(user.get());
                LOGGER.info("Register: User {} is now verified", user.get().getId());
                return Response.ok().build();
            }

            LOGGER.error("Register: User {} does not exist", verificationToken.get().getUserId());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.error("Register: There was a problem with the verification token");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("resend_token")
    public Response resendToken() {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){

            Optional<VerificationToken> verificationToken = us.getVerificationToken(user.get().getVerificationToken().get().getToken());

            if (verificationToken.isPresent()) {
                Optional<User> registered = Optional.ofNullable(verificationToken.get().getUser());
//                String appUrl = request.getRequestURL().toString();
//                appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());
                if (registered.isPresent()) {
//                    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), request.getLocale(), appUrl, true));
                    LOGGER.info("Register: Resending verification token for user {}", registered.get().getId());
                    return Response.ok().build();
                }

                LOGGER.error("Register: User {} does not exist", verificationToken.get().getUserId());
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("forgot_password")
    public Response forgotPassword(final UserDTO userDTO) {
        Optional<User> optionalUser = us.findByMail(userDTO.getMail());
        if (!optionalUser.isPresent()) {
            LOGGER.error("Register: No user found with mail {}", userDTO.getMail());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = optionalUser.get();
//        String appUrl = request.getRequestURL().toString();
//        appUrl = appUrl.substring(0, appUrl.indexOf(request.getRequestURI())).concat(request.getContextPath());

//        us.passwordMailing(user, appUrl);
        LOGGER.info("Register: Successfully sent recovery mail to {}", userDTO.getMail());
        return Response.ok().build();
    }

}

