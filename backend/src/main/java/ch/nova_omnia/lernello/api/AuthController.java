package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.UserLoginDTO;
import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.security.JwtUtil;
import ch.nova_omnia.lernello.service.UserService;
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
    @Autowired
    UserService userService;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param userLoginDTO The user to authenticate.
     * @return The JWT token.
     */
    @PostMapping("/signin")
    public LoggedInUserDTO authenticateUser(@RequestBody UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.username(), userLoginDTO.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isChangedPasswordStatus = userService.getChangedPasswordStatus(userDetails.getUsername());
        String token = jwtUtils.generateToken(userDetails.getUsername());
        int expirationDate = jwtUtils.getExpirationTime();

        return new LoggedInUserDTO(token, userDetails.getUsername(), isChangedPasswordStatus, expirationDate);
    }
}
