package ch.nova_omnia.lernello.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        authentication.getPrincipal();

        User user = userRepository.findByUsername(username);
        user.setToken(jwtUtil.generateToken(user.getUsername()));
        user.setExpires(jwtUtil.getExpirationTime());
        return user;
    }

    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user.isChangedPassword()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setChangedPassword(true);
        userRepository.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllTrainees() {
        return userRepository.findAllByRole(User.Role.TRAINEE);
    }
}
