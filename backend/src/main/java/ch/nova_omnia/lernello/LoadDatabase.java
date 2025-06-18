package ch.nova_omnia.lernello;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.BlockLanguage;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, LearningKitRepository learningKitRepository, LearningUnitRepository learningUnitRepository) {

        User johanna = new User("zubj@zhaw.ch", "Johanna", "Decurtins", encoder.encode("password"), null, Role.INSTRUCTOR);
        johanna.setChangedPassword(true);

        User frodo = new User("frodo@gmail.com", "Frodo", "Baggins", encoder.encode("password"), null, Role.INSTRUCTOR);
        frodo.setChangedPassword(true);

        User liebhart = new User("lieh@zhaw.ch", "Liebhart", "Daniel", encoder.encode("password"), null, Role.INSTRUCTOR);
        liebhart.setChangedPassword(true);

        User max = new User("max.sebastian@bluewin.ch", "Max", "Sebastian", encoder.encode("password"), null, Role.TRAINEE);
        max.setChangedPassword(true);

        User bruno = new User("bruno.frisch@gmail.com", "Bruno", "Frisch", encoder.encode("password"), null, Role.TRAINEE);
        bruno.setChangedPassword(true);

        User sara = new User("sara@gmail.com", "Sara", "Mueller", encoder.encode("password"), null, Role.TRAINEE);
        sara.setChangedPassword(true);

        User thomas = new User("thomas@gmail.com", "Thomas", "Weber", encoder.encode("password"), null, Role.TRAINEE);
        thomas.setChangedPassword(true);

        User lisa = new User("lisa@gmail.com", "Lisa", "Schmidt", encoder.encode("password"), null, Role.TRAINEE);
        lisa.setChangedPassword(true);

        User michael = new User("michael@gmail.com", "Michael", "Wagner", encoder.encode("password"), null, Role.TRAINEE);
        michael.setChangedPassword(true);

        User anna = new User("anna@gmail.com", "Anna", "Fischer", encoder.encode("password"), null, Role.TRAINEE);
        anna.setChangedPassword(true);

        User david = new User("david@gmail.com", "David", "Bauer", encoder.encode("password"), null, Role.TRAINEE);
        david.setChangedPassword(true);

        User julia = new User("julia@gmail.com", "Julia", "Hoffmann", encoder.encode("password"), null, Role.TRAINEE);
        julia.setChangedPassword(true);

        User peter = new User("peter@gmail.com", "Peter", "Koch", encoder.encode("password"), null, Role.TRAINEE);
        peter.setChangedPassword(true);

        User dante = new User("dante@gmail.com", "Dante", "Alighieri", encoder.encode("password"), null, Role.TRAINEE);
        dante.setChangedPassword(true);

        return (_) -> {
            if (!userRepository.existsByUsername(frodo.getUsername()))
                userRepository.save(frodo);
            if (!userRepository.existsByUsername(max.getUsername()))
                userRepository.save(max);
            if (!userRepository.existsByUsername(bruno.getUsername()))
                userRepository.save(bruno);
            if (!userRepository.existsByUsername(johanna.getUsername()))
                userRepository.save(johanna);
            if (!userRepository.existsByUsername(liebhart.getUsername()))
                userRepository.save(liebhart);
            if (!userRepository.existsByUsername(sara.getUsername()))
                userRepository.save(sara);
            if (!userRepository.existsByUsername(thomas.getUsername()))
                userRepository.save(thomas);
            if (!userRepository.existsByUsername(lisa.getUsername()))
                userRepository.save(lisa);
            if (!userRepository.existsByUsername(michael.getUsername()))
                userRepository.save(michael);
            if (!userRepository.existsByUsername(anna.getUsername()))
                userRepository.save(anna);
            if (!userRepository.existsByUsername(david.getUsername()))
                userRepository.save(david);
            if (!userRepository.existsByUsername(julia.getUsername()))
                userRepository.save(julia);
            if (!userRepository.existsByUsername(peter.getUsername()))
                userRepository.save(peter);
            if (!userRepository.existsByUsername(dante.getUsername()))
                userRepository.save(dante);

            LearningKit premadeKit = new LearningKit();
            premadeKit.setName("Ausrüstung");
            premadeKit.setDescription("In dieser Lerneinheit geht es um die Schacht- und Beckenausruestung, die in der Bergbauindustrie verwendet wird.");

            LearningUnit premadeUnit = new LearningUnit("Schwimmerdrossel Typ WSD", premadeKit);
            List<LearningUnit> learningUnits = premadeKit.getLearningUnits();
            learningUnits.add(premadeUnit);

            // Theorie 1: Hauptkomponenten
            TheoryBlock theory1 = new TheoryBlock(PreMadeLearningKitEn.Theory1_en_Title, 1, premadeUnit, PreMadeLearningKitEn.Theory1_en);
            TranslatedBlock theory1de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Theory1_de, theory1, PreMadeLearningKitDe.Theory1_de_Title);
            TranslatedBlock theory1fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Theory1_fr, theory1, PreMadeLearningKitFr.Theory1_fr_Title);
            TranslatedBlock theory1it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Theory1_it, theory1, PreMadeLearningKitIt.Theory1_it_Title);

            QuestionBlock question2 = new QuestionBlock(PreMadeLearningKitEn.Question2_en_Title, 2, premadeUnit, PreMadeLearningKitEn.Question2_en, PreMadeLearningKitEn.Answer2_en);
            TranslatedBlock question2de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question2_de, PreMadeLearningKitDe.Answer2_de, question2, PreMadeLearningKitDe.Question2_de_Title);
            TranslatedBlock question2fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question2_fr, PreMadeLearningKitFr.Answer2_fr, question2, PreMadeLearningKitFr.Question2_fr_Title);
            TranslatedBlock question2it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question2_it, PreMadeLearningKitIt.Answer2_it, question2, PreMadeLearningKitIt.Question2_it_Title);

            // Theorie 2: Funktion und Genauigkeit
            TheoryBlock theory2 = new TheoryBlock(PreMadeLearningKitEn.Theory2_en_Title, 3, premadeUnit, PreMadeLearningKitEn.Theory2_en);
            TranslatedBlock theory2de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Theory2_de, theory2, PreMadeLearningKitDe.Theory2_de_Title);
            TranslatedBlock theory2fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Theory2_fr, theory2, PreMadeLearningKitFr.Theory2_fr_Title);
            TranslatedBlock theory2it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Theory2_it, theory2, PreMadeLearningKitIt.Theory2_it_Title);

            QuestionBlock question3 = new QuestionBlock(PreMadeLearningKitEn.Question3_en_Title, 4, premadeUnit, PreMadeLearningKitEn.Question3_en, PreMadeLearningKitEn.Answer3_en);
            TranslatedBlock question3de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question3_de, PreMadeLearningKitDe.Answer3_de, question3, PreMadeLearningKitDe.Question3_de_Title);
            TranslatedBlock question3fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question3_fr, PreMadeLearningKitFr.Answer3_fr, question3, PreMadeLearningKitFr.Question3_fr_Title);
            TranslatedBlock question3it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question3_it, PreMadeLearningKitIt.Answer3_it, question3, PreMadeLearningKitIt.Question3_it_Title);

            QuestionBlock question4 = new QuestionBlock(PreMadeLearningKitEn.Question4_en_Title, 5, premadeUnit, PreMadeLearningKitEn.Question4_en, PreMadeLearningKitEn.Answer4_en);
            TranslatedBlock question4de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question4_de, PreMadeLearningKitDe.Answer4_de, question4, PreMadeLearningKitDe.Question4_de_Title);
            TranslatedBlock question4fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question4_fr, PreMadeLearningKitFr.Answer4_fr, question4, PreMadeLearningKitFr.Question4_fr_Title);
            TranslatedBlock question4it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question4_it, PreMadeLearningKitIt.Answer4_it, question4, PreMadeLearningKitIt.Question4_it_Title);
            
            QuestionBlock question9 = new QuestionBlock(PreMadeLearningKitEn.Question9_en_Title, 6, premadeUnit, PreMadeLearningKitEn.Question9_en, PreMadeLearningKitEn.Answer9_en);
            TranslatedBlock question9de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question9_de, PreMadeLearningKitDe.Answer9_de, question9, PreMadeLearningKitDe.Question9_de_Title);
            TranslatedBlock question9fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question9_fr, PreMadeLearningKitFr.Answer9_fr, question9, PreMadeLearningKitFr.Question9_fr_Title);
            TranslatedBlock question9it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question9_it, PreMadeLearningKitIt.Answer9_it, question9, PreMadeLearningKitIt.Question9_it_Title);
            
            QuestionBlock question10 = new QuestionBlock(PreMadeLearningKitEn.Question10_en_Title, 7, premadeUnit, PreMadeLearningKitEn.Question10_en, PreMadeLearningKitEn.Answer10_en);
            TranslatedBlock question10de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question10_de, PreMadeLearningKitDe.Answer10_de, question10, PreMadeLearningKitDe.Question10_de_Title);
            TranslatedBlock question10fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question10_fr, PreMadeLearningKitFr.Answer10_fr, question10, PreMadeLearningKitFr.Question10_fr_Title);
            TranslatedBlock question10it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question10_it, PreMadeLearningKitIt.Answer10_it, question10, PreMadeLearningKitIt.Question10_it_Title);  
            
            // Theorie 3: Einbau und Zweck
            TheoryBlock theory3 = new TheoryBlock(PreMadeLearningKitEn.Theory3_en_Title, 8, premadeUnit, PreMadeLearningKitEn.Theory3_en);
            TranslatedBlock theory3de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Theory3_de, theory3, PreMadeLearningKitDe.Theory3_de_Title);
            TranslatedBlock theory3fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Theory3_fr, theory3, PreMadeLearningKitFr.Theory3_fr_Title);
            TranslatedBlock theory3it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Theory3_it, theory3, PreMadeLearningKitIt.Theory3_it_Title);
            
            QuestionBlock question1 = new QuestionBlock(PreMadeLearningKitEn.Question1_en_Title, 9, premadeUnit, PreMadeLearningKitEn.Question1_en, PreMadeLearningKitEn.Answer1_en);
            TranslatedBlock question1de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question1_de, PreMadeLearningKitDe.Answer1_de, question1, PreMadeLearningKitDe.Question1_de_Title);
            TranslatedBlock question1fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question1_fr, PreMadeLearningKitFr.Answer1_fr, question1, PreMadeLearningKitFr.Question1_fr_Title);
            TranslatedBlock question1it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question1_it, PreMadeLearningKitIt.Answer1_it, question1, PreMadeLearningKitIt.Question1_it_Title);
            
            QuestionBlock question6 = new QuestionBlock(PreMadeLearningKitEn.Question6_en_Title, 10, premadeUnit, PreMadeLearningKitEn.Question6_en, PreMadeLearningKitEn.Answer6_en);
            TranslatedBlock question6de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question6_de, PreMadeLearningKitDe.Answer6_de, question6, PreMadeLearningKitDe.Question6_de_Title);
            TranslatedBlock question6fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question6_fr, PreMadeLearningKitFr.Answer6_fr, question6, PreMadeLearningKitFr.Question6_fr_Title);
            TranslatedBlock question6it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question6_it, PreMadeLearningKitIt.Answer6_it, question6, PreMadeLearningKitIt.Question6_it_Title);
            
            QuestionBlock question7 = new QuestionBlock(PreMadeLearningKitEn.Question7_en_Title, 11, premadeUnit, PreMadeLearningKitEn.Question7_en, PreMadeLearningKitEn.Answer7_en);
            TranslatedBlock question7de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question7_de, PreMadeLearningKitDe.Answer7_de, question7, PreMadeLearningKitDe.Question7_de_Title);
            TranslatedBlock question7fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question7_fr, PreMadeLearningKitFr.Answer7_fr, question7, PreMadeLearningKitFr.Question7_fr_Title);
            TranslatedBlock question7it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question7_it, PreMadeLearningKitIt.Answer7_it, question7, PreMadeLearningKitIt.Question7_it_Title);
            
            // Theorie 4: Genauigkeit und Material
            TheoryBlock theory4 = new TheoryBlock(PreMadeLearningKitEn.Theory4_en_Title, 12, premadeUnit, PreMadeLearningKitEn.Theory4_en);
            TranslatedBlock theory4de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Theory4_de, theory4, PreMadeLearningKitDe.Theory4_de_Title);
            TranslatedBlock theory4fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Theory4_fr, theory4, PreMadeLearningKitFr.Theory4_fr_Title);
            TranslatedBlock theory4it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Theory4_it, theory4, PreMadeLearningKitIt.Theory4_it_Title);
            
            QuestionBlock question5 = new QuestionBlock(PreMadeLearningKitEn.Question5_en_Title, 13, premadeUnit, PreMadeLearningKitEn.Question5_en, PreMadeLearningKitEn.Answer5_en);
            TranslatedBlock question5de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question5_de, PreMadeLearningKitDe.Answer5_de, question5, PreMadeLearningKitDe.Question5_de_Title);
            TranslatedBlock question5fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question5_fr, PreMadeLearningKitFr.Answer5_fr, question5, PreMadeLearningKitFr.Question5_fr_Title);
            TranslatedBlock question5it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question5_it, PreMadeLearningKitIt.Answer5_it, question5, PreMadeLearningKitIt.Question5_it_Title);
            
            QuestionBlock question8 = new QuestionBlock(PreMadeLearningKitEn.Question8_en_Title, 14, premadeUnit, PreMadeLearningKitEn.Question8_en, PreMadeLearningKitEn.Answer8_en);
            TranslatedBlock question8de = new TranslatedBlock(BlockLanguage.GERMAN, PreMadeLearningKitDe.Question8_de, PreMadeLearningKitDe.Answer8_de, question8, PreMadeLearningKitDe.Question8_de_Title);
            TranslatedBlock question8fr = new TranslatedBlock(BlockLanguage.FRENCH, PreMadeLearningKitFr.Question8_fr, PreMadeLearningKitFr.Answer8_fr, question8, PreMadeLearningKitFr.Question8_fr_Title);
            TranslatedBlock question8it = new TranslatedBlock(BlockLanguage.ITALIAN, PreMadeLearningKitIt.Question8_it, PreMadeLearningKitIt.Answer8_it, question8, PreMadeLearningKitIt.Question8_it_Title);
            
            List<Block> blocks = premadeUnit.getBlocks();
            blocks.add(theory1);
            blocks.add(theory1de);
            blocks.add(theory1fr);
            blocks.add(theory1it);

            blocks.add(theory2);
            blocks.add(theory2de);
            blocks.add(theory2fr);
            blocks.add(theory2it);

            blocks.add(theory3);
            blocks.add(theory3de);
            blocks.add(theory3fr);
            blocks.add(theory3it);

            blocks.add(theory4);
            blocks.add(theory4de);
            blocks.add(theory4fr);
            blocks.add(theory4it);

            blocks.add(question1);
            blocks.add(question1de);
            blocks.add(question1fr);
            blocks.add(question1it);

            blocks.add(question2);
            blocks.add(question2de);
            blocks.add(question2fr);
            blocks.add(question2it);

            blocks.add(question3);
            blocks.add(question3de);
            blocks.add(question3fr);
            blocks.add(question3it);

            blocks.add(question4);
            blocks.add(question4de);
            blocks.add(question4fr);
            blocks.add(question4it);

            blocks.add(question5);
            blocks.add(question5de);
            blocks.add(question5fr);
            blocks.add(question5it);

            blocks.add(question6);
            blocks.add(question6de);
            blocks.add(question6fr);
            blocks.add(question6it);

            blocks.add(question7);
            blocks.add(question7de);
            blocks.add(question7fr);
            blocks.add(question7it);

            blocks.add(question8);
            blocks.add(question8de);
            blocks.add(question8fr);
            blocks.add(question8it);

            blocks.add(question9);
            blocks.add(question9de);
            blocks.add(question9fr);
            blocks.add(question9it);

            blocks.add(question10);
            blocks.add(question10de);
            blocks.add(question10fr);
            blocks.add(question10it);

            learningKitRepository.save(premadeKit);
        };
    }
}
