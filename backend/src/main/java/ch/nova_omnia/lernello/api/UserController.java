package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/change-password")
    public @Valid PasswordStatusDTO changePassword(@RequestBody @Valid ChangePasswordDataDTO data) {
        String currentPassword = userService.getPassword(data.username());

        if (!isPasswordConfirmed(data.newPassword(), data.confirmPassword())) {
            return new PasswordStatusDTO(false);
        }
        if (!isOldPasswordValid(data.oldPassword(), currentPassword)) {
            return new PasswordStatusDTO(false);
        }
        if (isNewPasswordSameAsOld(data.newPassword(), currentPassword)) {
            return new PasswordStatusDTO(false);
        }

        userService.setChangedPasswordStatus(data.username(), passwordEncoder.encode(data.newPassword()));
        return new PasswordStatusDTO(true);
    }

    private boolean isPasswordConfirmed(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    private boolean isOldPasswordValid(String providedOldPassword, String storedPasswordHash) {
        return passwordEncoder.matches(providedOldPassword, storedPasswordHash);
    }

    private boolean isNewPasswordSameAsOld(String newPassword, String storedPasswordHash) {
        return passwordEncoder.matches(newPassword, storedPasswordHash);
    }
}