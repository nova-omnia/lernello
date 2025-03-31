package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('SCOPE_password:change')")
    public @Valid PasswordStatusDTO changePassword(
                                                   @RequestBody @Valid ChangePasswordDataDTO data, @AuthenticationPrincipal UserDetails userDetails) {
        boolean status = userService.changePassword(userDetails.getUsername(), data.newPassword());
        return new PasswordStatusDTO(status);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public @Valid List<ParticipantUserDTO> getAllUsers() {
        return userService.findAll().stream()
                .map(user -> new ParticipantUserDTO(user.getUuid(), user.getUsername()))
                .toList();
    }
}
