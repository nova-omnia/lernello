package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service from Spring Security for handling user details and authentication easily.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads a user by username.
     *
     * @param username The username of the user to load.
     * @return The user details.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getUserScopes(user)
        );
    }

    private List<GrantedAuthority> getUserScopes(User user) {
        List<GrantedAuthority> scopes = new ArrayList<>();

        scopes.add(new SimpleGrantedAuthority("SCOPE_self:read"));
        if (!user.isChangedPassword()) {
            scopes.add(new SimpleGrantedAuthority("SCOPE_password:write"));
            return scopes;
        }
        scopes.add(new SimpleGrantedAuthority("SCOPE_self:write"));

        switch (user.getRole()) {
            case INSTRUCTOR -> {
                scopes.add(new SimpleGrantedAuthority("SCOPE_folders:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_folders:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_files:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_files:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_blocks:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_blocks:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_kits:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_user:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_user:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_learningUnit:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_learningUnit:write"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_kits:read"));
            }
            case TRAINEE -> {
                scopes.add(new SimpleGrantedAuthority("SCOPE_files:read"));
                scopes.add(new SimpleGrantedAuthority("SCOPE_blocks:read"));
            }
        }
        return scopes;
    }
}
