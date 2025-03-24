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
import ch.nova_omnia.lernello.repository.LearningUnitRepository;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LearningUnitTest {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private LearningKitRepository learningKitRepository;

    @Autowired
    private LearningUnitRepository learningUnitRepository;

    private static Validator validator;

    private Folder testFolder;
    private LearningKit testLearningKit;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        testFolder = new Folder("Test Folder");
        testFolder = folderRepository.save(testFolder);
        testLearningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, testFolder);
        testLearningKit = learningKitRepository.save(testLearningKit);
    }

    // Section: Basic LearningUnit Creation Tests
    @Test
    public void testLearningUnitCreationWithNameAndLearningKit() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", testLearningKit);
        learningUnit = learningUnitRepository.save(learningUnit);
        assertThat(learningUnit).isNotNull();
        assertThat(learningUnit.getName()).isEqualTo("Test Learning Unit");
        assertThat(learningUnit.getLearningKit()).isEqualTo(testLearningKit);
    }

    @Test
    public void testLearningUnitCreationWithNullLearningKit() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", null);
        Set<ConstraintViolation<LearningUnit>> violations = validator.validate(learningUnit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("must not be null"));
    }

    // Section: Validation Tests
    @Test
    public void testLearningUnitNameConstraints() {
        LearningUnit learningUnit = new LearningUnit("", testLearningKit);
        Set<ConstraintViolation<LearningUnit>> violations = validator.validate(learningUnit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("must not be blank"));

        learningUnit = new LearningUnit("ab", testLearningKit);
        violations = validator.validate(learningUnit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));

        learningUnit = new LearningUnit("a".repeat(41), testLearningKit);
        violations = validator.validate(learningUnit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    @Test
    public void testValidationOnUpdate() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", testLearningKit);
        learningUnit = learningUnitRepository.save(learningUnit);
        learningUnit.setName("ab");
        Set<ConstraintViolation<LearningUnit>> violations = validator.validate(learningUnit);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    // Section: UUID Generation Test
    @Test
    public void testLearningUnitUuidGeneration() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", testLearningKit);
        learningUnit = learningUnitRepository.save(learningUnit);
        assertThat(learningUnit.getUuid()).isNotNull();
    }

    // Section: Association Tests
    @Test
    public void testLearningUnitAssociationWithLearningKit() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", testLearningKit);
        learningUnit = learningUnitRepository.save(learningUnit);
        assertThat(learningUnit.getLearningKit()).isEqualTo(testLearningKit);
    }
}