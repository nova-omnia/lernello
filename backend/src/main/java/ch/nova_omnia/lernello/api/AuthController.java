package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.UserLoginDTO;
import ch.nova_omnia.lernello.dto.response.user.LoggedInUserDTO;
import ch.nova_omnia.lernello.mapper.UserLoginMapper;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserLoginMapper userLoginMapper;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param userLoginDTO The user to authenticate.
     * @return The JWT token.
     */
    @PostMapping("/signin")
    public LoggedInUserDTO authenticateUser(@RequestBody UserLoginDTO userLoginDTO) {
        User authenticateUser = userService.authenticate(userLoginDTO.username(), userLoginDTO.password());
        return userLoginMapper.toDTO(authenticateUser);
    }
}
