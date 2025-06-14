package ch.nova_omnia.lernello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
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
            if (!userRepository.existsByUsername(frodo.getUsername())) userRepository.save(frodo);
            if (!userRepository.existsByUsername(max.getUsername())) userRepository.save(max);
            if (!userRepository.existsByUsername(bruno.getUsername())) userRepository.save(bruno);
            if (!userRepository.existsByUsername(johanna.getUsername())) userRepository.save(johanna);
            if (!userRepository.existsByUsername(liebhart.getUsername())) userRepository.save(liebhart);
            if (!userRepository.existsByUsername(sara.getUsername())) userRepository.save(sara);
            if (!userRepository.existsByUsername(thomas.getUsername())) userRepository.save(thomas);
            if (!userRepository.existsByUsername(lisa.getUsername())) userRepository.save(lisa);
            if (!userRepository.existsByUsername(michael.getUsername())) userRepository.save(michael);
            if (!userRepository.existsByUsername(anna.getUsername())) userRepository.save(anna);
            if (!userRepository.existsByUsername(david.getUsername())) userRepository.save(david);
            if (!userRepository.existsByUsername(julia.getUsername())) userRepository.save(julia);
            if (!userRepository.existsByUsername(peter.getUsername())) userRepository.save(peter);
            if (!userRepository.existsByUsername(dante.getUsername())) userRepository.save(dante);


            LearningKit premadeKit = new LearningKit();
            premadeKit.setName("Premade Kit");
            premadeKit.setDescription( "test descrition" );

            LearningUnit premadeUnit = new LearningUnit("Erste Lerneinheit", premadeKit);
            premadeKit.learningUnits.add(premadeUnit);

            TheoryBlock theory1 = new TheoryBlock("Theory 1",1,premadeUnit,PremadeLearningKit.Theory1);
            TheoryBlock theory2 = new TheoryBlock("Theory 2",2,premadeUnit,PremadeLearningKit.Theory2);
            TheoryBlock theory3 = new TheoryBlock("Theory 3",3,premadeUnit,PremadeLearningKit.Theory3);
            TheoryBlock theory4 = new TheoryBlock("Theory 4",4,premadeUnit,PremadeLearningKit.Theory4);
            QuestionBlock question1 = new QuestionBlock("Frage 1", 5, premadeUnit, PremadeLearningKit.Question1, PremadeLearningKit.Answer1);
            QuestionBlock question2 = new QuestionBlock("Frage 2", 6, premadeUnit, PremadeLearningKit.Question2, PremadeLearningKit.Answer2);
            QuestionBlock question3 = new QuestionBlock("Frage 3", 7, premadeUnit, PremadeLearningKit.Question3, PremadeLearningKit.Answer3);
            QuestionBlock question4 = new QuestionBlock("Frage 4", 8, premadeUnit, PremadeLearningKit.Question4, PremadeLearningKit.Answer4);
            QuestionBlock question5 = new QuestionBlock("Frage 5", 9, premadeUnit, PremadeLearningKit.Question5, PremadeLearningKit.Answer5);
            QuestionBlock question6 = new QuestionBlock("Frage 6", 10, premadeUnit, PremadeLearningKit.Question6, PremadeLearningKit.Answer6);
            QuestionBlock question7 = new QuestionBlock("Frage 7", 11, premadeUnit, PremadeLearningKit.Question7, PremadeLearningKit.Answer7);
            QuestionBlock question8 = new QuestionBlock("Frage 8", 12, premadeUnit, PremadeLearningKit.Question8, PremadeLearningKit.Answer8);
            QuestionBlock question9 = new QuestionBlock("Frage 9", 13, premadeUnit, PremadeLearningKit.Question9, PremadeLearningKit.Answer9);
            QuestionBlock question10 = new QuestionBlock("Frage 10", 14, premadeUnit, PremadeLearningKit.Question10, PremadeLearningKit.Answer10);

            premadeUnit.blocks.add(theory1);
            premadeUnit.blocks.add(theory2);
            premadeUnit.blocks.add(theory3);
            premadeUnit.blocks.add(theory4);
            premadeUnit.blocks.add(question1);
            premadeUnit.blocks.add(question2);
            premadeUnit.blocks.add(question3);
            premadeUnit.blocks.add(question4);
            premadeUnit.blocks.add(question5);
            premadeUnit.blocks.add(question6);
            premadeUnit.blocks.add(question7);
            premadeUnit.blocks.add(question8);
            premadeUnit.blocks.add(question9);
            premadeUnit.blocks.add(question10);

            learningKitRepository.save(premadeKit);
        };
    }
}
