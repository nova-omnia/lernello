package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomUserDetailsService service;

    @Test
    void loadUserByUsernameShouldReturnScopesWhenPasswordNotChanged() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pwd");
        user.setChangedPassword(false);

        when(userRepository.findByUsername("user1")).thenReturn(user);

        UserDetails details = service.loadUserByUsername("user1");

        assertThat(details.getUsername()).isEqualTo("user1");
        assertThat(details.getPassword()).isEqualTo("pwd");
        List<String> auths = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        assertThat(auths).containsExactlyInAnyOrder("SCOPE_self:read", "SCOPE_password:write");
    }

    @Test
    void loadUserByUsernameShouldReturnTraineeScopesWhenPasswordChanged() {
        User user = new User();
        user.setUsername("trainee");
        user.setPassword("pwd2");
        user.setChangedPassword(true);
        user.setRole(Role.TRAINEE);

        when(userRepository.findByUsername("trainee")).thenReturn(user);

        UserDetails details = service.loadUserByUsername("trainee");

        List<String> auths = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        assertThat(auths).contains("SCOPE_self:read", "SCOPE_self:write", "SCOPE_kits:read", "SCOPE_progress:read");
    }

    @Test
    void loadUserByUsernameShouldReturnInstructorScopesWhenPasswordChanged() {
        User user = new User();
        user.setUsername("instr");
        user.setPassword("pwd3");
        user.setChangedPassword(true);
        user.setRole(Role.INSTRUCTOR);

        when(userRepository.findByUsername("instr")).thenReturn(user);

        UserDetails details = service.loadUserByUsername("instr");

        List<String> auths = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        assertThat(auths).contains("SCOPE_self:read", "SCOPE_self:write", "SCOPE_folders:write", "SCOPE_user:write", "SCOPE_progress:write");
    }

    @Test
    void loadUserByUsernameShouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(null);

        assertThatThrownBy(() -> service.loadUserByUsername("missing"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User Not Found");
    }

    @Test
    void getUserIdByUsernameShouldReturnUuid() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setUsername("u1");
        user.setUuid(id);

        when(userRepository.findByUsername("u1")).thenReturn(user);

        UUID result = service.getUserIdByUsername("u1");

        assertThat(result).isEqualTo(id);
    }

    @Test
    void getUserIdByUsernameShouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("none")).thenReturn(null);

        assertThatThrownBy(() -> service.getUserIdByUsername("none"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User Not Found");
    }
}
