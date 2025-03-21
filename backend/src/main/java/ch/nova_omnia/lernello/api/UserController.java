package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/changePassword")
    public @Valid PasswordStatusDTO changePassword(@Valid ChangePasswordDataDTO changePasswordDataDTO) {
        String newPassword = changePasswordDataDTO.newPassword();
        String confirmPassword = changePasswordDataDTO.confirmPassword();

        if (!passwordEncoder.matches(confirmPassword, newPassword)) {
            return new PasswordStatusDTO(false);
        }
        String username = changePasswordDataDTO.username();
        String oldPassword = userService.getPassword(username);
        String newHashedPassword = passwordEncoder.encode(newPassword);

        if (!passwordEncoder.matches(newHashedPassword, oldPassword)) {
            return new PasswordStatusDTO(false);
        }
        userService.changePassword(username, newHashedPassword);
        return new PasswordStatusDTO(true);
    }
}
