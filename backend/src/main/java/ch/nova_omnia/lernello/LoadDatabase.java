package ch.nova_omnia.lernello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.FileRepository;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.service.BlockService;
import ch.nova_omnia.lernello.service.LearningKitService;
import ch.nova_omnia.lernello.service.LearningUnitService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(FolderRepository folderRepository, UserRepository userRepository, LearningKitRepository learningKitRepository, LearningUnitRepository learningUnitRepository, FileRepository fileRepository, LearningKitService learningKitService, LearningUnitService learningUnitService, BlockService blockService) {

        User frodo = new User("frodo@gmail.com", "Frodo", "Baggins", encoder.encode("password"), null, Role.INSTRUCTOR);
        frodo.setChangedPassword(true);

        User max = new User("max.sebastian@bluewin.ch", "Max", "Sebastian", encoder.encode("password"), null, Role.TRAINEE);
        max.setChangedPassword(true);
        User bruno = new User("bruno.frisch@gmail.com", "Bruno", "Frisch", encoder.encode("password"), null, Role.TRAINEE);
        bruno.setChangedPassword(true);

        return (_) -> {
            userRepository.save(frodo);
            userRepository.save(max);
            userRepository.save(bruno);
        };
    }
}
