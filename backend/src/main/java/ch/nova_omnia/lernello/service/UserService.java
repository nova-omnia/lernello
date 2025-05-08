package ch.nova_omnia.lernello.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.user.RefreshToken;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.RefreshTokenRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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
        user.setToken(jwtUtil.generateToken(user.getUuid()));
        UUID jitUuid = UUID.randomUUID();
        String refreshToken = jwtUtil.generateRefreshToken(jitUuid);
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setJti(jitUuid.toString());
        refreshTokenEntity.setUser(user);
        refreshTokenRepository.save(refreshTokenEntity);

        user.setRefreshToken(refreshToken);
        ZonedDateTime expirationTime = ZonedDateTime.now().plus(jwtUtil.getExpirationTime());
        user.setExpires(expirationTime);
        ZonedDateTime refreshExpirationTime = ZonedDateTime.now().plus(jwtUtil.getRefreshExpirationTime());
        user.setRefreshExpires(refreshExpirationTime);
        return user;
    }

    public User getUserFromUserDetails(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername());
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

    public List<User> findAllByIds(List<UUID> uuids) {
        if (uuids == null) {
            return null;
        }
        return userRepository.findAllById(uuids);
    }

    public String setLocale(String username, String locale) {
        User user = userRepository.findByUsername(username);
        user.setLocale(locale);
        userRepository.save(user);
        return locale;
    }

    public List<User> findAllTrainees() {
        return userRepository.findAllByRole(Role.TRAINEE);
    }

    public User addTrainee(String username, String name, String surname) {
        User user = new User();
        user.setUsername(username);
        user.setSurname(surname);
        user.setName(name);
        user.setRole(Role.TRAINEE);
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setChangedPassword(false);
        userRepository.save(user);
        return user;
    }

    public void deleteTrainee(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    public User editTrainee(String username, String name, String surname) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setUsername(username);
            user.setName(name);
            user.setSurname(surname);
            userRepository.save(user);
        }
        return user;
    }

    public Role getCurrentRole(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getRole();
        }
        return null;
    }
}
