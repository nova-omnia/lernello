package ch.nova_omnia.lernello.service;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        authentication.getPrincipal();

        User user = this.findByUsername(username);
        user.setToken(jwtUtil.generateToken(user.getUsername()));
        user.setExpires(jwtUtil.getExpirationTime());
        return user;
    }

    public void setAuthCookie(HttpServletResponse response, User authenticatedUser) {
        String token = authenticatedUser.getToken();
        Duration expirationTime = Duration.ofMillis(authenticatedUser.getExpires());

        ResponseCookie jwtCookie = ResponseCookie.from("lernello_auth_token", token).httpOnly(true)  // Prevent JavaScript access (XSS protection)
                .secure(true)    // Only send over HTTPS
                .sameSite("None") // Prevent CSRF
                .path("/")       // Make cookie available across the site
                .maxAge(expirationTime) // Set expiration
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
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
}
