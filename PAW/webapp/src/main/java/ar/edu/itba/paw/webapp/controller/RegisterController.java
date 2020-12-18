package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.register.OnRegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Context
    private UriInfo uriInfo;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response register (final UserAddDTO userDTO) {
        try {
            User registeredUser = us.create(userDTO.getUsername(), userDTO.getMail(), userDTO.getPassword());
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, LocaleContextHolder.getLocale(), uriInfo.getRequestUri().toString() , false));
            LOGGER.info("Register: User '{}' registered successfully with id {}", registeredUser.getUsername(), registeredUser.getId());

            return login(new JwtRequestDTO(userDTO.getUsername(), userDTO.getPassword()));
        }
        catch (UserAlreadyExistException uaeEx) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private Response login(JwtRequestDTO jwtRequestDTO) {
        try {
            authenticate(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword());
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtRequestDTO.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        Optional<User> user = us.findByUsername(jwtRequestDTO.getUsername());
        return Response.ok(new JwtResponseDTO(token,user.get())).build();

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

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
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response resendToken() {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.isPresent()){

            Optional<VerificationToken> verificationToken = us.getVerificationToken(user.get().getVerificationToken().get().getToken());

            if (verificationToken.isPresent()) {
                Optional<User> registered = Optional.ofNullable(verificationToken.get().getUser());

                if (registered.isPresent()) {
                    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), LocaleContextHolder.getLocale(), uriInfo.getBaseUri().toString() + "register", true));
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
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response forgotPassword(final UserDTO userDTO) {
        Optional<User> optionalUser = us.findByMail(userDTO.getMail());
        if (!optionalUser.isPresent()) {
            LOGGER.error("Register: No user found with mail {}", userDTO.getMail());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        us.passwordMailing(optionalUser.get(), uriInfo.getBaseUri().toString() + "register");
        LOGGER.info("Register: Successfully sent recovery mail to {}", userDTO.getMail());
        return Response.ok().build();
    }

    @GET
    @Path("forgot_password")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response forgotPasswordToken(@QueryParam("token") String token) {
        String[] strings = token.split("-a_d-ss-");
        Long userId = Long.valueOf(strings[strings.length - 1]);

        Optional<User> optionalUser = us.findById(userId);

        if (optionalUser.isPresent()) {
            PasswordDTO dto = new PasswordDTO();
            dto.setToken(token);
            return Response.ok(dto).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}

