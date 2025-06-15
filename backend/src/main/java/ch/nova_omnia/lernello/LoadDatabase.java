package ch.nova_omnia.lernello;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.scorable.QuestionBlock;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, LearningKitRepository learningKitRepository) {

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
            premadeKit.setDescription("In dieser Lerneinheit geht es um die Schacht- und Beckenausruestung, " + "die in der Bergbauindustrie verwendet wird.");

            LearningUnit premadeUnit = new LearningUnit("Schwimmerdrossel Typ WSD", premadeKit);
            List<LearningUnit> learningUnits = premadeKit.getLearningUnits();
            learningUnits.add(premadeUnit);

            // Theorie 1: Hauptkomponenten
            TheoryBlock theory1 = new TheoryBlock(PreMadeLearningKitEn.Theory1_en_Title, 1, premadeUnit, PreMadeLearningKitEn.Theory1_en);
            QuestionBlock question2 = new QuestionBlock(PreMadeLearningKitEn.Question2_en_Title, 2, premadeUnit, PreMadeLearningKitEn.Question2_en, PreMadeLearningKitEn.Answer2_en);

            // Theorie 2: Funktion und Genauigkeit
            TheoryBlock theory2 = new TheoryBlock(PreMadeLearningKitEn.Theory2_en_Title, 3, premadeUnit, PreMadeLearningKitEn.Theory2_en);
            QuestionBlock question3 = new QuestionBlock(PreMadeLearningKitEn.Question3_en_Title, 4, premadeUnit, PreMadeLearningKitEn.Question3_en, PreMadeLearningKitEn.Answer3_en);
            QuestionBlock question4 = new QuestionBlock(PreMadeLearningKitEn.Question4_en_Title, 5, premadeUnit, PreMadeLearningKitEn.Question4_en, PreMadeLearningKitEn.Answer4_en);
            QuestionBlock question9 = new QuestionBlock(PreMadeLearningKitEn.Question9_en_Title, 6, premadeUnit, PreMadeLearningKitEn.Question9_en, PreMadeLearningKitEn.Answer9_en);
            QuestionBlock question10 = new QuestionBlock(PreMadeLearningKitEn.Question10_en_Title, 7, premadeUnit, PreMadeLearningKitEn.Question10_en, PreMadeLearningKitEn.Answer10_en);

            // Theorie 3: Einbau und Zweck
            TheoryBlock theory3 = new TheoryBlock(PreMadeLearningKitEn.Theory3_en_Title, 8, premadeUnit, PreMadeLearningKitEn.Theory3_en);
            QuestionBlock question1 = new QuestionBlock(PreMadeLearningKitEn.Question1_en_Title, 9, premadeUnit, PreMadeLearningKitEn.Question1_en, PreMadeLearningKitEn.Answer1_en);
            QuestionBlock question6 = new QuestionBlock(PreMadeLearningKitEn.Question6_en_Title, 10, premadeUnit, PreMadeLearningKitEn.Question6_en, PreMadeLearningKitEn.Answer6_en);
            QuestionBlock question7 = new QuestionBlock(PreMadeLearningKitEn.Question7_en_Title, 11, premadeUnit, PreMadeLearningKitEn.Question7_en, PreMadeLearningKitEn.Answer7_en);

            // Theorie 4: Genauigkeit und Material
            TheoryBlock theory4 = new TheoryBlock(PreMadeLearningKitEn.Theory4_en_Title, 12, premadeUnit, PreMadeLearningKitEn.Theory4_en);
            QuestionBlock question5 = new QuestionBlock(PreMadeLearningKitEn.Question5_en_Title, 13, premadeUnit, PreMadeLearningKitEn.Question5_en, PreMadeLearningKitEn.Answer5_en);
            QuestionBlock question8 = new QuestionBlock(PreMadeLearningKitEn.Question8_en_Title, 14, premadeUnit, PreMadeLearningKitEn.Question8_en, PreMadeLearningKitEn.Answer8_en);

            List<Block> blocks = premadeUnit.getBlocks();
            blocks.add(theory1);
            blocks.add(theory2);
            blocks.add(theory3);
            blocks.add(theory4);
            blocks.add(question1);
            blocks.add(question2);
            blocks.add(question3);
            blocks.add(question4);
            blocks.add(question5);
            blocks.add(question6);
            blocks.add(question7);
            blocks.add(question8);
            blocks.add(question9);
            blocks.add(question10);

            learningKitRepository.save(premadeKit);
        };
    }
}
