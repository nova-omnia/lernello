package ch.nova_omnia.lernello.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {
    private final UserService userService;

    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('SCOPE_password:write')")
    public @Valid PasswordStatusDTO changePassword(
                                                   @RequestBody @Valid ChangePasswordDataDTO data, @AuthenticationPrincipal UserDetails userDetails) {
        boolean status = userService.changePassword(userDetails.getUsername(), data.newPassword());
        return new PasswordStatusDTO(status);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public @Valid List<ParticipantUserDTO> getAllUsers() {
        return userService.findAll().stream().map(user -> new ParticipantUserDTO(user.getUuid(), user.getUsername())).toList();
    }
}
