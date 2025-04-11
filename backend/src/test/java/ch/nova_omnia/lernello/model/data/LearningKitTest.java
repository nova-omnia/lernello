package ch.nova_omnia.lernello.model.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@DataJpaTest
public class LearningKitTest {


    @Autowired
    private LearningKitRepository learningKitRepository;

    private static Validator validator;


    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    // Helper method to validate constraints
    private void assertConstraintViolation(Set<ConstraintViolation<LearningKit>> violations, String property, Class<?> annotation) {
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(property) && v.getConstraintDescriptor().getAnnotation().annotationType().equals(annotation));
    }

    // Section: Basic LearningKit Creation Tests
    @Test
    public void testLearningKitCreationWithName() {
        LearningKit learningKit = new LearningKit("Test Learning Kit");
        learningKit = learningKitRepository.save(learningKit);
        assertThat(learningKit).isNotNull();
        assertThat(learningKit.getName()).isEqualTo("Test Learning Kit");
    }

    // Section: Validation Tests
    @Test
    public void testLearningKitNameConstraints() {
        LearningKit learningKit = new LearningKit("");
        Set<ConstraintViolation<LearningKit>> violations = validator.validate(learningKit);
        assertConstraintViolation(violations, "name", NotBlank.class);

        learningKit = new LearningKit("ab");
        violations = validator.validate(learningKit);
        assertConstraintViolation(violations, "name", Size.class);

        learningKit = new LearningKit("a".repeat(41));
        violations = validator.validate(learningKit);
        assertConstraintViolation(violations, "name", Size.class);
    }

    @Test
    public void testValidationOnUpdate() {
        LearningKit learningKit = new LearningKit("Test Learning Kit");
        learningKit = learningKitRepository.save(learningKit);
        learningKit.setName("ab");
        Set<ConstraintViolation<LearningKit>> violations = validator.validate(learningKit);
        assertConstraintViolation(violations, "name", Size.class);
    }

    // Section: UUID Generation Test
    @Test
    public void testLearningKitUuidGeneration() {
        LearningKit learningKit = new LearningKit("Test Learning Kit");
        learningKit = learningKitRepository.save(learningKit);
        assertThat(learningKit.getUuid()).isNotNull();
    }

    // Section: Association Tests
    @Test
    public void testLearningUnitsAssociation() {
        LearningKit learningKit = new LearningKit("Test Learning Kit");
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
        LearningKit learningKit = new LearningKit("Test Learning Kit");
        learningKit = learningKitRepository.save(learningKit);
        LearningUnit learningUnit = new LearningUnit("Test Learning Unit", learningKit);
        learningKit.getLearningUnits().add(learningUnit);
        learningKit = learningKitRepository.save(learningKit);
        learningKitRepository.delete(learningKit);
        assertThat(learningKitRepository.findById(learningKit.getUuid())).isEmpty();
    }
}
