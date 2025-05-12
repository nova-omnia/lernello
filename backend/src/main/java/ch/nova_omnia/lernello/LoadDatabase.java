package ch.nova_omnia.lernello;

import java.util.List;

import ch.nova_omnia.lernello.dto.request.block.blockActions.AddBlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionDTO;
import ch.nova_omnia.lernello.dto.request.block.blockActions.BlockActionType;
import ch.nova_omnia.lernello.dto.request.block.create.CreateTheoryBlockDTO;
import ch.nova_omnia.lernello.service.LearningKitService;
import ch.nova_omnia.lernello.service.LearningUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.Folder;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.FileRepository;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import static ch.nova_omnia.lernello.model.data.block.BlockType.THEORY;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(FolderRepository folderRepository, UserRepository userRepository,
                                   LearningKitRepository learningKitRepository, LearningUnitRepository learningUnitRepository,
                                   FileRepository fileRepository, LearningKitService learningKitService,
                                   LearningUnitService learningUnitService) {

        Folder bungo = new Folder("Bungo Baggins");
        Folder bilbo = new Folder("Bilbo Baggins");
        bilbo.setParentFolder(bungo);

        User frodo = new User("frodo@gmail.com", "Frodo", "Baggins", encoder.encode("password"), null, Role.INSTRUCTOR);
        frodo.setChangedPassword(true);

        User max = new User("max.sebastian@bluewin.ch", "Max", "Sebastian", encoder.encode("password"), null, Role.TRAINEE);
        User bruno = new User("bruno.frisch@gmail.com", "Bruno", "Frisch", encoder.encode("password"), null, Role.TRAINEE);
        User leon = new User("brodbeckleon@gmail.com", "Leon", "Brodbeck", encoder.encode("password"), null, Role.TRAINEE);
        User pascal = new User("pascal.Dorta@gmx.ch", "Pascal", "Dorta", encoder.encode("password"), null, Role.TRAINEE);
        User anja = new User("anja.wigger@bluewin.ch", "Anja", "Wigger", encoder.encode("password"), null, Role.TRAINEE);
        User fabian = new User("fabian_balo@gmail.com", "Fabian", "Balo", encoder.encode("password"), null, Role.TRAINEE);
        User dani = new User("malek_dani78@gmail.com", "Dani", "Malek", encoder.encode("password"), null, Role.TRAINEE);

        LearningKit learningKit = new LearningKit("Learning Kit 1");
        LearningUnit learningUnit = new LearningUnit("Learning Unit 1", learningKit);

        CreateTheoryBlockDTO createTheoryBlockDTO1 = new CreateTheoryBlockDTO(THEORY, "Theory Block 0", 0, theoryBlockContent);
        AddBlockActionDTO addBlockActionDTO = new AddBlockActionDTO(BlockActionType.ADD_BLOCK, 0, "tempid:0", createTheoryBlockDTO1);
        List<BlockActionDTO> actions = List.of(addBlockActionDTO);

        return (_) -> {
            log.info("Preloading folders");
            folderRepository.save(bungo);
            folderRepository.save(bilbo);
            log.info("Preloading users");
            userRepository.save(frodo);
            userRepository.save(max);
            userRepository.save(bruno);
            userRepository.save(leon);
            userRepository.save(pascal);
            userRepository.save(anja);
            userRepository.save(fabian);
            userRepository.save(dani);
            log.info("Preloading learning kit");
            learningKit.setParticipants(List.of(max, bruno, leon, pascal, anja, fabian, dani));
            learningKitRepository.save(learningKit);
            log.info("Preloaded learning kit with id: " + learningKit.getUuid());
            log.info("Preloading learning unit");
            learningUnitRepository.save(learningUnit);
            log.info("Preloaded learning unit with id: " + learningUnit.getUuid());
            learningUnitService.applyBlockActions(learningUnit.getUuid(), actions);
            log.info("Preloaded folder with id: " + bungo.getUuid());
            log.info("Preloaded folder with id: " + bilbo.getUuid());
            log.info("Preloaded user with id: " + frodo.getUuid());
        };
    }

    private final String theoryBlockContent = """
        # Markdown Test \s
        \s
        ## Überschrift Ebene 2 \s
        \s
        ### Überschrift Ebene 3 \s
        \s
        **Fetter Text** \s
        \s
        *Kursiver Text* \s
               \s
        ~~Durchgestrichener Text~~ \s
        \s
        - Ungeordnete Liste Punkt 1 \s
               \s
        - Ungeordnete Liste Punkt 2 \s
               \s
          - Verschachtelter Punkt \s
        \s
        1. Geordnete Liste Punkt 1 \s
               \s
        2. Geordnete Liste Punkt 2 \s
        \s
        [Link zu Google](https://www.google.com)\s
        \s
        ```java
               \s
        public class Main {
               \s
          public static void main(String[] args) {
               \s
            System.out.println("Hello World");
               \s
          }
               \s
        }
               \s
        ```
        \s
        > Blockquote \s
        > Zweite Zeile \s
        \s
        | Tabelle | Test | \s
               \s
        |---------|------| \s
               \s
        | Zelle 1 | Zelle 2 | \s
        \s
        Horizontale Linie: \s
        \s
        --- \s
        \s
        ![](https://th-thumbnailer.cdn-si-edu.com/FL1u6Fs5T1tUo1RAuUZmdw5ssXA=/1000x750/filters:no_upscale():focal(518x341:519x342)/https://tf-cmsv2-smithsonianmag-media.s3.amazonaws.com/filer_public/1a/3d/1a3d1353-7317-4f5f-8b89-23cd066a4535/jason_gulley_wildlife_photographer_of_the_year.jpg)
        \s
       \s""";
}
