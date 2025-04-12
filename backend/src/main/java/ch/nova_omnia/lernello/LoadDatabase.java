package ch.nova_omnia.lernello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Autowired
    PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(FolderRepository folderRepository, UserRepository userRepository, LearningKitRepository learningKitRepository, LearningUnitRepository learningUnitRepository
    ) {
        Folder bungo = new Folder("Bungo Baggins");
        Folder bilbo = new Folder("Bilbo Baggins");
        bilbo.setParentFolder(bungo);

        User frodo = new User("frodo@gmail.com", encoder.encode("password"), null, User.Role.INSTRUCTOR);
        frodo.setChangedPassword(true);

        LearningKit learningKit = new LearningKit("Learning Kit 1");
        LearningUnit learningUnit = new LearningUnit("Learning Unit 1", learningKit);

        return (_) -> {
            log.info("Preloading folders");
            folderRepository.save(bungo);
            folderRepository.save(bilbo);
            log.info("Preloading users");
            userRepository.save(frodo);
            log.info("Preloading learning kit");
            learningKitRepository.save(learningKit);
            log.info("Preloading learning unit");
            learningUnitRepository.save(learningUnit);

            // log ids
            log.info("Preloaded folder with id: " + bungo.getUuid());
            log.info("Preloaded folder with id: " + bilbo.getUuid());
            log.info("Preloaded user with id: " + frodo.getUuid());
            log.info("Preloaded learning kit with id: " + learningKit.getUuid());
            log.info("Preloaded learning unit with id: " + learningUnit.getUuid());
        };
    }
}
