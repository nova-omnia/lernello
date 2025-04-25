package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.UUID;

import ch.nova_omnia.lernello.dto.response.user.UserRoleDTO;
import ch.nova_omnia.lernello.mapper.user.RoleMapper;
import ch.nova_omnia.lernello.model.data.user.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.request.user.CreateParticipantDTO;
import ch.nova_omnia.lernello.dto.request.user.UserLocaleDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.dto.response.user.UserInfoDTO;
import ch.nova_omnia.lernello.mapper.user.ParticipantUserMapper;
import ch.nova_omnia.lernello.mapper.user.UserInfoMapper;
import ch.nova_omnia.lernello.mapper.user.UserLocaleMapper;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {
    private final UserService userService;
    private final RoleMapper roleMapper;
    private final UserLocaleMapper userLocaleMapper;
    private final UserInfoMapper userInfoMapper;
    private final ParticipantUserMapper participantUserMapper;

    @PostMapping("/password")
    @PreAuthorize("hasAuthority('SCOPE_password:write')")
    public @Valid PasswordStatusDTO changePassword(
                                                   @RequestBody @Valid ChangePasswordDataDTO data, @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean status = userService.changePassword(userDetails.getUsername(), data.newPassword());
        return new PasswordStatusDTO(status);
    }

    @GetMapping("/trainees")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public List<@Valid ParticipantUserDTO> getAllTrainees() {
        return userService.findAllTrainees().stream().map(participantUserMapper::toDTO).toList();
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('SCOPE_self:read')")
    public @Valid UserInfoDTO getUserInfo(
                                          @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        return userInfoMapper.toDTO(user);
    }

    @PostMapping("/trainee")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public @Valid ParticipantUserDTO addTrainee(
                                                @RequestBody @Valid CreateParticipantDTO traineeDetails
    ) {
        User trainee = userService.addTrainee(traineeDetails.username(), traineeDetails.name(), traineeDetails.surname());
        return participantUserMapper.toDTO(trainee);
    }

    @DeleteMapping("/trainee/{id}")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public void deleteTrainee(
                              @PathVariable UUID id
    ) {
        userService.deleteTrainee(id);
    }

    @PostMapping("/locale")
    @PreAuthorize("hasAuthority('SCOPE_self:write')")
    public @Valid UserLocaleDTO setUserLocale(
                                              @RequestBody @Valid UserLocaleDTO data, @AuthenticationPrincipal UserDetails userDetails
    ) {
        String locale = userService.setLocale(userDetails.getUsername(), data.locale());
        return userLocaleMapper.toDTO(locale);
    }


    @PatchMapping("/trainee")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public @Valid ParticipantUserDTO editTrainee(
                                                 @RequestBody @Valid CreateParticipantDTO traineeDetails
    ) {
        User trainee = userService.editTrainee(traineeDetails.username(), traineeDetails.name(), traineeDetails.surname());
        return participantUserMapper.toDTO(trainee);
    }

    @PostMapping("/getCurrentRole")
    @PreAuthorize("hasAuthority('SCOPE_self:read')")
    public @Valid UserRoleDTO getCurrentRole(
                                        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Role role = userService.getCurrentRole(userDetails.getUsername());
        return roleMapper.toDTO(role);
    }
}
