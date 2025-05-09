package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LearningKitRepository learningKitRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public User update(UUID uuid, User user) {
        User existingUser = userRepository.findByUuid(uuid);
        if (existingUser == null) {
            throw new IllegalArgumentException("User with UUID " + user.getUuid() + " not found.");
        }
        removeInstructorFromLearningKits(user, existingUser);

        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    public User authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        authentication.getPrincipal();

        User user = this.findByUsername(username);
        user.setToken(jwtUtil.generateToken(user.getUsername()));
        ZonedDateTime expirationTime = ZonedDateTime.now().plus(jwtUtil.getExpirationTime());
        user.setExpires(expirationTime);
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

    public User createUser(String username, String name, String surname, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setSurname(surname);
        user.setName(name);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(generateRandomPassword()));
        user.setChangedPassword(false);
        userRepository.save(user);
        return user;
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
        return userRepository.findAllByRoleOrderBySurnameAscNameAsc(Role.TRAINEE);
    }

    public List<User> findAllInstructors() {
        return userRepository.findAllByRoleOrderBySurnameAscNameAsc(Role.INSTRUCTOR);
    }

    public void deleteUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new IllegalArgumentException("User with UUID " + uuid + " not found.");
        }
        removeUserAllLearningKits(user);
        userRepository.delete(user);
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

    public String generateRandomPassword() {
        SecureRandom rng = new SecureRandom();
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rng.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void removeUserAllLearningKits(User user) {
        List<LearningKit> kitsWithUser = learningKitRepository.findAllByParticipantsContains(user);
        for (LearningKit kit : kitsWithUser) {
            kit.getParticipants().remove(user);
        }
        learningKitRepository.saveAll(kitsWithUser);
    }

    private void removeInstructorFromLearningKits(User user, User existingUser) {
        if (user.getRole() == Role.INSTRUCTOR) {
            removeUserAllLearningKits(existingUser);
        }
    }
}
