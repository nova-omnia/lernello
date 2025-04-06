package ch.nova_omnia.lernello.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.user.UserLoginDTO;
import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.mapper.user.UserLoginMapper;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling authentication requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthRestController {
    private final UserService userService;
    private final UserLoginMapper userLoginMapper;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param userLoginDTO The user to authenticate.
     * @return The JWT token.
     */
    @PostMapping("/signin")
    public @Valid LoggedInUserDTO signin(@RequestBody @Valid UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User authenticatedUser = userService.authenticate(userLoginDTO.username(), userLoginDTO.password());

        userService.setAuthCookie(response, authenticatedUser);

        return userLoginMapper.toDTO(authenticatedUser);
    }


    /**
     * Sets the authentication cookie for the user.
     *
     * @param response The HTTP response.
     */
    @PostMapping("/set-cookie")
    @PreAuthorize("hasAuthority('SCOPE_authcookie:read')")
    public @Valid LoggedInUserDTO setCookie(@RequestBody @Valid LoggedInUserDTO loggedInUserDTO, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        authenticatedUser.setToken(loggedInUserDTO.token());
        authenticatedUser.setExpires(loggedInUserDTO.expires());
        userService.setAuthCookie(response, authenticatedUser);

        return loggedInUserDTO;
    }
}
