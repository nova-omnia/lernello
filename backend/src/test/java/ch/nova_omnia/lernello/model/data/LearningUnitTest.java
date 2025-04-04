package ch.nova_omnia.lernello.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ch.nova_omnia.lernello.repository.FolderRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;

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

    // Helper method to validate constraints
    private void assertConstraintViolation(Set<ConstraintViolation<LearningUnit>> violations, String property, Class<?> annotation) {
        assertThat(violations)
            .anyMatch(v -> v.getPropertyPath().toString().equals(property) &&
                           v.getConstraintDescriptor().getAnnotation().annotationType().equals(annotation));
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
        assertConstraintViolation(violations, "learningKit", NotNull.class);
    }

    // Section: Validation Tests
    @Test
    public void testLearningUnitNameConstraints() {
        LearningUnit learningUnit = new LearningUnit("", testLearningKit);
        Set<ConstraintViolation<LearningUnit>> violations = validator.validate(learningUnit);
        assertConstraintViolation(violations, "name", NotBlank.class);

        learningUnit = new LearningUnit("ab", testLearningKit);
        violations = validator.validate(learningUnit);
        assertConstraintViolation(violations, "name", Size.class);

        learningUnit = new LearningUnit("a".repeat(41), testLearningKit);
        violations = validator.validate(learningUnit);
        assertConstraintViolation(violations, "name", Size.class);
    }

    @Test
    public void testValidationOnUpdate() {
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", testLearningKit);
        learningUnit = learningUnitRepository.save(learningUnit);
        learningUnit.setName("ab");
        Set<ConstraintViolation<LearningUnit>> violations = validator.validate(learningUnit);
        assertConstraintViolation(violations, "name", Size.class);
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