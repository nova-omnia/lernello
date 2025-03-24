package ch.nova_omnia.lernello.model.data;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LearningKitTest {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private LearningKitRepository learningKitRepository;

    private static Validator validator;

    private Folder testFolder;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        testFolder = new Folder("Test Folder");
        testFolder = folderRepository.save(testFolder);
    }

    // Section: Basic LearningKit Creation Tests
    @Test
    public void testLearningKitCreationWithNameAndFolder() {
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        learningKit = learningKitRepository.save(learningKit);
        assertThat(learningKit).isNotNull();
        assertThat(learningKit.getName()).isEqualTo("Test Learning Kit");
        assertThat(learningKit.getFolder()).isEqualTo(testFolder);
    }

    // Section: Validation Tests
    @Test
    public void testLearningKitNameConstraints() {
        LearningKit learningKit = new LearningKit("", LearningKit.Language.GERMAN, testFolder);
        Set<ConstraintViolation<LearningKit>> violations = validator.validate(learningKit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("must not be blank"));

        learningKit = new LearningKit("ab", LearningKit.Language.GERMAN, testFolder);
        violations = validator.validate(learningKit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));

        learningKit = new LearningKit("a".repeat(41), LearningKit.Language.GERMAN, testFolder);
        violations = validator.validate(learningKit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    @Test
    public void testValidationOnUpdate() {
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        learningKit = learningKitRepository.save(learningKit);
        learningKit.setName("ab");
        Set<ConstraintViolation<LearningKit>> violations = validator.validate(learningKit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    // Section: UUID Generation Test
    @Test
    public void testLearningKitUuidGeneration() {
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        learningKit = learningKitRepository.save(learningKit);
        assertThat(learningKit.getUuid()).isNotNull();
    }

    // Section: Association Tests
    @Test
    public void testLearningUnitsAssociation() {
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        learningKit = learningKitRepository.save(learningKit);
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", learningKit);
        learningKit.getLearningUnits().add(learningUnit);
        learningKit = learningKitRepository.save(learningKit);
    
        // Fetch the learning kit again to ensure the association is persisted
        LearningKit fetchedLearningKit = learningKitRepository.findById(learningKit.getUuid()).orElse(null);
        assertThat(fetchedLearningKit).isNotNull();
        assertThat(fetchedLearningKit.getLearningUnits()).hasSize(1);
        assertThat(fetchedLearningKit.getLearningUnits().iterator().next().getName()).isEqualTo("Test Learning Unit");
    }

    // Section: Orphan Removal Tests
    @Test
    public void testOrphanRemovalForLearningUnits() {
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        learningKit = learningKitRepository.save(learningKit);
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", learningKit);
        learningKit.getLearningUnits().add(learningUnit);
        learningKit = learningKitRepository.save(learningKit);
        learningKitRepository.delete(learningKit);
        assertThat(learningKitRepository.findById(learningKit.getUuid())).isEmpty();
    }
}