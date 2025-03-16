package ch.nova_omnia.lernello;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.repository.FolderRepository;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(FolderRepository repository) {
        Folder bungo = new Folder("Bungo Baggins");
        Folder bilbo = new Folder("Bilbo Baggins");
        Folder frodo = new Folder("Frodo Baggins");

        frodo.setParentFolder(bilbo);
        bilbo.setParentFolder(bungo);

        return (args) -> {
            log.info("Preloading folders");
            repository.save(bungo);
            repository.save(bilbo);
            repository.save(frodo);
        };
    }
}
