package ch.nova_omnia.lernello;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.User;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.UserRepository;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Autowired
    PasswordEncoder encoder;


    @Bean
    CommandLineRunner initDatabase(FolderRepository folderRepository, UserRepository userRepository) {
        Folder bungo = new Folder("Bungo Baggins");
        Folder bilbo = new Folder("Bilbo Baggins");
        bilbo.setParentFolder(bungo);

        User frodo = new User(null, "frodo@gmail.com", encoder.encode("password"));


        return (_) -> {
            log.info("Preloading folders");
            folderRepository.save(bungo);
            folderRepository.save(bilbo);
            log.info("Preloading users");
            userRepository.save(frodo);
        };
    }
}
