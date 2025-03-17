package ch.nova_omnia.lernello.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;

/**
 * Service from Spring Security for handling user details and authentication easily.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

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
        // switch (user.getRole()) {
        //     case INSTRUCTOR -> {
        scopes.add(new SimpleGrantedAuthority("SCOPE_folders:read"));
        scopes.add(new SimpleGrantedAuthority("SCOPE_folders:write"));
        // }
        return scopes;
    }
}
