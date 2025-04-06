package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.request.user.UserLocaleDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.mapper.user.ParticipantUserMapper;
import ch.nova_omnia.lernello.mapper.user.UserLocaleMapper;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {
    private final UserService userService;
    private final UserLocaleMapper userLocaleMapper;
    private final ParticipantUserMapper participantUserMapper;

    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('SCOPE_password:write')")
    public @Valid PasswordStatusDTO changePassword(
            @RequestBody @Valid ChangePasswordDataDTO data,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean status = userService.changePassword(userDetails.getUsername(), data.newPassword());
        return new PasswordStatusDTO(status);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public @Valid List<ParticipantUserDTO> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(user -> participantUserMapper.toDTO(user.getUuid(), user.getUsername()))
                .toList();
    }

    @PostMapping("/locale")
    public @Valid UserLocaleDTO setLocale(
            @RequestBody @Valid UserLocaleDTO data,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String locale = userService.setLocale(userDetails.getUsername(), data.locale());
        return userLocaleMapper.toDTO(locale);
    }
}
