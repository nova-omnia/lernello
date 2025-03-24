package ch.nova_omnia.lernello.model.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
public class FolderTest {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private LearningKitRepository learningKitRepository;

    private static Validator validator;

    private Folder rootFolder;
    private Folder parentFolder;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        rootFolder = new Folder("Root Folder");
        parentFolder = new Folder("Parent Folder");
    }

    // Section: Basic Folder Creation Tests
    @Test
    public void testFolderCreationWithName() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        assertThat(folder).isNotNull();
        assertThat(folder.getName()).isEqualTo("Test Folder");
    }

    @Test
    public void testFolderCreationWithParentFolder() {
        parentFolder = folderRepository.save(parentFolder);
        Folder childFolder = new Folder("Child Folder", parentFolder);
        childFolder = folderRepository.save(childFolder);
        assertThat(childFolder).isNotNull();
        assertThat(childFolder.getName()).isEqualTo("Child Folder");
        assertThat(childFolder.getParentFolder()).isEqualTo(parentFolder);
    }

    // Section: Validation Tests
    @Test
    public void testFolderNameConstraints() {
        Folder folder = new Folder("");
        Set<ConstraintViolation<Folder>> violations = validator.validate(folder);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("must not be blank"));

        folder = new Folder("ab");
        violations = validator.validate(folder);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));

        folder = new Folder("a".repeat(41));
        violations = validator.validate(folder);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    @Test
    public void testValidationOnUpdate() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        folder.setName("ab");
        Set<ConstraintViolation<Folder>> violations = validator.validate(folder);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("size must be between 3 and 40"));
    }

    // Section: UUID Generation Test
    @Test
    public void testFolderUuidGeneration() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        assertThat(folder.getUuid()).isNotNull();
    }

    // Section: Association Tests
    @Test
    public void testSubFoldersAssociation() {
        parentFolder = folderRepository.save(parentFolder);
        Folder childFolder = new Folder("Child Folder", parentFolder);
        childFolder = folderRepository.save(childFolder);
        parentFolder.getSubFolders().add(childFolder);
        parentFolder = folderRepository.save(parentFolder);
        assertThat(parentFolder.getSubFolders()).contains(childFolder);
    }

    @Test
    public void testLearningKitsAssociation() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, folder);
        learningKit = learningKitRepository.save(learningKit); // Save the LearningKit entity
        folder.getLearningKits().add(learningKit);
        folder = folderRepository.save(folder);
        assertThat(folder.getLearningKits()).contains(learningKit);
    }

    // Section: Orphan Removal Tests
    @Test
    public void testOrphanRemoval() {
        parentFolder = folderRepository.save(parentFolder);
        Folder childFolder = new Folder("Child Folder", parentFolder);
        childFolder = folderRepository.save(childFolder);
        parentFolder.getSubFolders().add(childFolder);
        parentFolder = folderRepository.save(parentFolder);
        folderRepository.delete(parentFolder);
        assertThat(folderRepository.findById(childFolder.getUuid())).isEmpty();
    }

    @Test
    public void testOrphanRemovalForLearningKits() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        LearningKit learningKit = new LearningKit("Test Learning Kit", LearningKit.Language.GERMAN, folder);
        learningKit = learningKitRepository.save(learningKit);
        folder.getLearningKits().add(learningKit);
        folder = folderRepository.save(folder);
        folderRepository.delete(folder);
        assertThat(learningKitRepository.findById(learningKit.getUuid())).isEmpty();
    }

    // Section: Hierarchy Tests
    @Test
    public void testEmptySubFoldersAndLearningKits() {
        Folder folder = new Folder("Test Folder");
        folder = folderRepository.save(folder);
        assertThat(folder).isNotNull();
        assertThat(folder.getSubFolders()).isEmpty();
        assertThat(folder.getLearningKits()).isEmpty();
    }

    @Test
    public void testDeepHierarchy() {
        rootFolder = folderRepository.save(rootFolder);
        Folder level1Folder = new Folder("Level 1 Folder", rootFolder);
        level1Folder = folderRepository.save(level1Folder);
        Folder level2Folder = new Folder("Level 2 Folder", level1Folder);
        level2Folder = folderRepository.save(level2Folder);
        assertThat(level2Folder).isNotNull();
        assertThat(level2Folder.getParentFolder()).isEqualTo(level1Folder);
        assertThat(level2Folder.getParentFolder().getParentFolder()).isEqualTo(rootFolder);
    }

    // Section: Null Parent Folder Test
    @Test
    public void testNullParentFolder() {
        Folder folder = new Folder("Folder with Null Parent", null);
        folder = folderRepository.save(folder);
        assertThat(folder).isNotNull();
        assertThat(folder.getName()).isEqualTo("Folder with Null Parent");
        assertThat(folder.getParentFolder()).isNull();
    }

    // Section: Circular Relationship Test
    @Test
    public void testCircularRelationship() {
        rootFolder = folderRepository.save(rootFolder);
        Folder level1Folder = new Folder("Level 1 Folder", rootFolder);
        level1Folder = folderRepository.save(level1Folder);
        Folder level2Folder = new Folder("Level 2 Folder", level1Folder);
        level2Folder = folderRepository.save(level2Folder);

        // Attempt to create a circular relationship
        try {
            rootFolder.setParentFolder(level2Folder);
            folderRepository.save(rootFolder);
            fail("Expected an IllegalArgumentException due to circular reference");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Circular reference detected: A folder cannot be its own ancestor.");
        }
    }
}