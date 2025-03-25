package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public boolean changePassword(UUID uuid, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByUuid(uuid);

        if (isValidPasswordChange(newPassword, confirmPassword, oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private boolean isValidPasswordChange(String newPassword, String confirmPassword, String oldPassword, String storedPasswordHash) {
        return isPasswordConfirmed(newPassword, confirmPassword) &&
                isOldPasswordValid(oldPassword, storedPasswordHash) &&
                !isNewPasswordSameAsOld(newPassword, storedPasswordHash);
    }

    private boolean isPasswordConfirmed(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    private boolean isOldPasswordValid(String providedOldPassword, String storedPasswordHash) {
        return passwordEncoder.matches(providedOldPassword, storedPasswordHash);
    }

    private boolean isNewPasswordSameAsOld(String newPassword, String storedPasswordHash) {
        return passwordEncoder.matches(newPassword, storedPasswordHash);
    }
}
