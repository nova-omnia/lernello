package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.user.ChangePasswordDataDTO;
import ch.nova_omnia.lernello.dto.request.user.CreateParticipantDTO;
import ch.nova_omnia.lernello.dto.request.user.CreateUserDTO;
import ch.nova_omnia.lernello.dto.request.user.UpdateUserDTO;
import ch.nova_omnia.lernello.dto.request.user.UserLocaleDTO;
import ch.nova_omnia.lernello.dto.response.user.ParticipantUserDTO;
import ch.nova_omnia.lernello.dto.response.user.PasswordStatusDTO;
import ch.nova_omnia.lernello.dto.response.user.UserInfoDTO;
import ch.nova_omnia.lernello.dto.response.user.UserResDTO;
import ch.nova_omnia.lernello.mapper.user.ParticipantUserMapper;
import ch.nova_omnia.lernello.mapper.user.UserInfoMapper;
import ch.nova_omnia.lernello.mapper.user.UserLocaleMapper;
import ch.nova_omnia.lernello.mapper.user.UserMapper;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.service.EmailService;
import ch.nova_omnia.lernello.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {
    private final UserService userService;
    private final UserLocaleMapper userLocaleMapper;
    private final UserInfoMapper userInfoMapper;
    private final ParticipantUserMapper participantUserMapper;
    private final UserMapper userMapper;
    private final EmailService emailService;

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

    @GetMapping("/instructors")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public List<@Valid ParticipantUserDTO> getAllInstructors() {
        return userService.findAllInstructors().stream().map(participantUserMapper::toDTO).toList();
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('SCOPE_self:read')")
    public @Valid UserInfoDTO getUserInfo(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        return userInfoMapper.toDTO(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    public @Valid UserResDTO getUser(@PathVariable UUID id) {
        User user = userService.findByUuid(id);
        return userMapper.toDTO(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public @Valid UserResDTO editUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO updateUserDTO) {
        User user = userMapper.toEntity(updateUserDTO);
        User updatedUser = userService.update(id, user);
        return userMapper.toDTO(updatedUser);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public @Valid UserResDTO createUser(
        @RequestBody @Valid CreateUserDTO userDTO
    ) {
        User user = userService.createUser(userDTO.username(), userDTO.name(), userDTO.surname(), userDTO.role());
        return userMapper.toDTO(user);
    }

    @PatchMapping("/reset/{id}")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public @Valid UUID resetUserPassword(@PathVariable UUID id) {
        User user = userService.findByUuid(id);
        emailService.sendNewLoginData(user);
        return id;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_user:write')")
    public UUID deleteUser(
        @PathVariable UUID id
    ) {
        userService.deleteUser(id);
        return id;
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
}
