package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.dto.JwtRequestDTO;
import ar.edu.itba.paw.webapp.dto.JwtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("login")
@Component
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PawUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @POST
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response login(JwtRequestDTO jwtRequestDTO) {
        try {
            authenticate(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword());
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtRequestDTO.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return Response.ok(new JwtResponseDTO(token)).build();

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


}
