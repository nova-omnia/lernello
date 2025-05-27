package ch.nova_omnia.lernello.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LearningKitRepository learningKitRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    private User existingUser;

    @BeforeEach
    void initUser() {
        existingUser = new User();
        existingUser.setUuid(UUID.randomUUID());
        existingUser.setUsername("user@example.com");
        existingUser.setName("First");
        existingUser.setSurname("Last");
        existingUser.setRole(Role.TRAINEE);
        existingUser.setChangedPassword(false);
        existingUser.setLocale("en");
        existingUser.setPassword("oldPwd");
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        when(userRepository.findByUsername("u")).thenReturn(existingUser);

        User result = userService.findByUsername("u");

        assertThat(result).isSameAs(existingUser);
    }

    @Test
    void findByUuidDelegatesToRepository() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByUuid(id)).thenReturn(existingUser);

        User result = userService.findByUuid(id);

        assertThat(result).isSameAs(existingUser);
    }

    @Test
    void updateThrowsOnMissingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByUuid(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.update(id, existingUser));
    }

    @Test
    void updateNonInstructorUpdatesFields() {
        UUID id = existingUser.getUuid();
        when(userRepository.findByUuid(id)).thenReturn(existingUser);
        User input = new User();
        input.setUuid(id);
        input.setName("New");
        input.setSurname("Name");
        input.setRole(Role.TRAINEE);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updated = userService.update(id, input);

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getSurname()).isEqualTo("Name");
        assertThat(updated.getRole()).isEqualTo(Role.TRAINEE);
        verify(learningKitRepository, never()).saveAll(any());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateInstructorRemovesFromLearningKits() {
        UUID id = existingUser.getUuid();
        when(userRepository.findByUuid(id)).thenReturn(existingUser);
        User input = new User();
        input.setUuid(id);
        input.setUsername("instr@example.com");
        input.setName("Inst");
        input.setSurname("Ructor");
        input.setRole(Role.INSTRUCTOR);

        LearningKit kit = new LearningKit();
        kit.setTrainees(new ArrayList<>(Collections.singletonList(existingUser)));
        when(learningKitRepository.findAllByTraineesContains(existingUser))
            .thenReturn(Collections.singletonList(kit));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updated = userService.update(id, input);

        assertThat(kit.getTrainees()).doesNotContain(existingUser);
        verify(learningKitRepository).saveAll(Collections.singletonList(kit));
        verify(userRepository).save(existingUser);
        assertThat(updated.getRole()).isEqualTo(Role.INSTRUCTOR);
    }

    @Test
    void authenticatePopulatesTokenAndExpiry() {
        String username = existingUser.getUsername();
        String password = "pwd";
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password))).thenReturn(auth);
        when(userRepository.findByUsername(username)).thenReturn(existingUser);
        when(jwtUtil.generateToken(username)).thenReturn("token");
        when(jwtUtil.getExpirationTime()).thenReturn(Duration.ofHours(1));

        ZonedDateTime start = ZonedDateTime.now();
        User result = userService.authenticate(username, password);
        ZonedDateTime expiry = result.getExpires();

        assertThat(result.getToken()).isEqualTo("token");
        assertThat(Duration.between(start, expiry).toHours()).isEqualTo(1);
    }

    @Test
    void getUserFromUserDetailsFetchesByUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("u");
        when(userRepository.findByUsername("u")).thenReturn(existingUser);

        User result = userService.getUserFromUserDetails(userDetails);

        assertThat(result).isSameAs(existingUser);
    }

    @Test
    void changePasswordReturnsFalseIfAlreadyChanged() {
        existingUser.setChangedPassword(true);
        when(userRepository.findByUsername(existingUser.getUsername()))
            .thenReturn(existingUser);

        boolean result = userService.changePassword(existingUser.getUsername(), "newPwd");

        assertThat(result).isFalse();
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePasswordEncodesAndSaves() {
        existingUser.setChangedPassword(false);
        when(userRepository.findByUsername(existingUser.getUsername()))
            .thenReturn(existingUser);
        when(passwordEncoder.encode("newPwd")).thenReturn("encoded");

        boolean result = userService.changePassword(existingUser.getUsername(), "newPwd");

        assertThat(result).isTrue();
        assertThat(existingUser.isChangedPassword()).isTrue();
        assertThat(existingUser.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(existingUser);
    }

    @Test
    void createUserSetsAllFieldsAndSaves() {
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        userService.createUser("a@b.c", "N", "S", Role.TRAINEE);

        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("a@b.c");
        assertThat(saved.getName()).isEqualTo("N");
        assertThat(saved.getSurname()).isEqualTo("S");
        assertThat(saved.getRole()).isEqualTo(Role.TRAINEE);
        assertThat(saved.getPassword()).isEqualTo("hash");
        assertThat(saved.isChangedPassword()).isFalse();
    }

    @Test
    void findAllByIdsReturnsNullIfInputNull() {
        assertThat(userService.findAllByIds(null)).isNull();
    }

    @Test
    void findAllByIdsDelegatesToRepository() {
        List<UUID> ids = List.of(UUID.randomUUID());
        List<User> users = Collections.singletonList(existingUser);
        when(userRepository.findAllById(ids)).thenReturn(users);

        List<User> result = userService.findAllByIds(ids);

        assertThat(result).isSameAs(users);
    }

    @Test
    void setLocaleUpdatesUserAndReturnsLocale() {
        when(userRepository.findByUsername("u")).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        String locale = userService.setLocale("u", "de");

        assertThat(locale).isEqualTo("de");
        assertThat(existingUser.getLocale()).isEqualTo("de");
        verify(userRepository).save(existingUser);
    }

    @Test
    void listTraineesDelegatesToRepository() {
        List<User> trainees = Collections.singletonList(existingUser);
        when(userRepository.findAllByRoleOrderBySurnameAscNameAsc(Role.TRAINEE))
            .thenReturn(trainees);

        List<User> result = userService.findAllTrainees();

        assertThat(result).isSameAs(trainees);
    }

    @Test
    void listInstructorsDelegatesToRepository() {
        List<User> instructors = Collections.singletonList(existingUser);
        when(userRepository.findAllByRoleOrderBySurnameAscNameAsc(Role.INSTRUCTOR))
            .thenReturn(instructors);

        List<User> result = userService.findAllInstructors();

        assertThat(result).isSameAs(instructors);
    }

    @Test
    void deleteUserThrowsOnMissingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findByUuid(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(id));
    }

    @Test
    void deleteUserRemovesFromKitsAndDeletes() {
        User u = existingUser;
        when(userRepository.findByUuid(u.getUuid())).thenReturn(u);
        LearningKit kit = new LearningKit();
        kit.setTrainees(new ArrayList<>(List.of(u)));
        when(learningKitRepository.findAllByTraineesContains(u))
            .thenReturn(List.of(kit));

        userService.deleteUser(u.getUuid());

        assertThat(kit.getTrainees()).doesNotContain(u);
        verify(learningKitRepository).saveAll(List.of(kit));
        verify(userRepository).delete(u);
    }

    @Test
    void editTraineeUpdatesExistingUser() {
        when(userRepository.findByUsername("u")).thenReturn(existingUser);

        User result = userService.editTrainee("u", "NY", "NZ");

        assertThat(result).isSameAs(existingUser);
        assertThat(existingUser.getName()).isEqualTo("NY");
        assertThat(existingUser.getSurname()).isEqualTo("NZ");
        verify(userRepository).save(existingUser);
    }

    @Test
    void editTraineeReturnsNullIfUserNotFound() {
        when(userRepository.findByUsername("x")).thenReturn(null);

        User result = userService.editTrainee("x", "A", "B");

        assertThat(result).isNull();
    }

    @Test
    void generateRandomPasswordReturnsEightCharString() {
        String pwd = userService.generateRandomPassword();

        assertThat(pwd).hasSize(8);
    }
}
