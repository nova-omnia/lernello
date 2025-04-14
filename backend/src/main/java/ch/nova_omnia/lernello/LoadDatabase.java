package ch.nova_omnia.lernello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.FileRepository;
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
    CommandLineRunner initDatabase(FolderRepository folderRepository, UserRepository userRepository, LearningKitRepository learningKitRepository, LearningUnitRepository learningUnitRepository,
    FileRepository fileRepository) {
        Folder bungo = new Folder("Bungo Baggins");
        Folder bilbo = new Folder("Bilbo Baggins");
        bilbo.setParentFolder(bungo);

        User frodo = new User("frodo@gmail.com", "Frodo", "Baggins", encoder.encode("password"), null, User.Role.INSTRUCTOR);
        frodo.setChangedPassword(true);


        File file1 = new File( "file1.txt");
        File file2 = new File( "file2.txt");
        File file3 = new File( "file3.txt");
        File file4 = new File( "file4.txt");
        File file5 = new File( "file5.txt");
        File file6 = new File( "file6.txt");
        File file7 = new File( "file7.txt");
        File file8 = new File( "file8.txt");
        File file9 = new File( "file9.txt");
        File file10 = new File( "file10.txt");

        User max = new User("max.sebastian@bluewin.ch", "Max", "Sebastian", encoder.encode("password"), null, User.Role.TRAINEE);
        User bruno = new User("bruno.frisch@gmail.com", "Bruno", "Frisch", encoder.encode("password"), null, User.Role.TRAINEE);
        User leon = new User("brodbeckleon@gmail.com", "Leon", "Brodbeck", encoder.encode("password"), null, User.Role.TRAINEE);
        User pascal = new User("pascal.Dorta@gmx.ch", "Pascal", "Dorta", encoder.encode("password"), null, User.Role.TRAINEE);
        User anja = new User("anja.wigger@bluewin.ch","Anja", "Wigger", encoder.encode("password"), null, User.Role.TRAINEE);
        User fabian = new User("fabian_balo@gmail.com", "Fabian", "Balo", encoder.encode("password"), null, User.Role.TRAINEE);
        User dani = new User("malek_dani78@gmail.com", "Dani", "Malek", encoder.encode("password"), null, User.Role.TRAINEE);
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
            fileRepository.save(file1);
            fileRepository.save(file2);
            fileRepository.save(file3);
            fileRepository.save(file4);
            fileRepository.save(file5);
            fileRepository.save(file6);
            fileRepository.save(file7);
            fileRepository.save(file8);
            fileRepository.save(file9);
            fileRepository.save(file10);
            userRepository.save(max);
            userRepository.save(bruno);
            userRepository.save(leon);
            userRepository.save(pascal);
            userRepository.save(anja);
            userRepository.save(fabian);
            userRepository.save(dani);
            // log ids
            log.info("Preloaded folder with id: " + bungo.getUuid());
            log.info("Preloaded folder with id: " + bilbo.getUuid());
            log.info("Preloaded user with id: " + frodo.getUuid());
            log.info("Preloaded learning kit with id: " + learningKit.getUuid());
            log.info("Preloaded learning unit with id: " + learningUnit.getUuid());
        };
    }
}
