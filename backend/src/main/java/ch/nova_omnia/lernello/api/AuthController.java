package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.response.UserDTO;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtils;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param user The user to authenticate.
     * @return The JWT token.
     */
    @PostMapping("/signin")
    public UserDTO authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new UserDTO(jwtUtils.generateToken(userDetails.getUsername()), userDetails.getUsername(), jwtUtils.getExpirationTime());
    }
}
