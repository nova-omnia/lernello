package ch.nova_omnia.lernello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {

        User frodo = new User("frodo@gmail.com", "Frodo", "Baggins", encoder.encode("password"), null, Role.INSTRUCTOR);
        frodo.setChangedPassword(true);

        User max = new User("max.sebastian@bluewin.ch", "Max", "Sebastian", encoder.encode("password"), null, Role.TRAINEE);
        max.setChangedPassword(true);

        User bruno = new User("bruno.frisch@gmail.com", "Bruno", "Frisch", encoder.encode("password"), null, Role.TRAINEE);
        bruno.setChangedPassword(true);

        User johanna = new User("zubj@zhaw.ch", "Johanna", "Decurtins", encoder.encode("password"), null, Role.INSTRUCTOR);
        johanna.setChangedPassword(true);

        return (_) -> {
            userRepository.save(frodo);
            userRepository.save(max);
            userRepository.save(bruno);
            userRepository.save(johanna);
        };
    }
}
