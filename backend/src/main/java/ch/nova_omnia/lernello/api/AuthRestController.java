package ch.nova_omnia.lernello.api;

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
    public @Valid LoggedInUserDTO signin(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        User authenticatedUser = userService.authenticate(userLoginDTO.username(), userLoginDTO.password());
        return userLoginMapper.toDTO(authenticatedUser);
    }
}
