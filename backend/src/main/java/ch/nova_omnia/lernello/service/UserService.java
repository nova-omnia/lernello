package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean getChangedPasswordStatus(String username) {
        User user = userRepository.findByUsername(username);
        return user.isChangedPassword();
    }

    public String getPassword(String username) {
        User user = userRepository.findByUsername(username);
        return user.getPassword();
    }

    public void changePassword(String username, String password) {
        User user = userRepository.getUserByUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }
}
