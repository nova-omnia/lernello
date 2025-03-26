package ch.nova_omnia.lernello.model.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import ch.nova_omnia.lernello.repository.FileRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SpringBootTest
public class FileTest {

    private static Validator validator;

    private File testFile;
    @Autowired
    private FileRepository fileRepository;


    @Value("${storage.path}")
    private String storagePath;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        testFile = new File("Test_File.pdf");
    }

    // Helper method to validate constraints
    private void assertConstraintViolation(Set<ConstraintViolation<File>> violations, String property, Class<?> annotation) {
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(property) && v.getConstraintDescriptor().getAnnotation().annotationType().equals(annotation));
    }

    @Test
    public void testFileCreationWithName() {
        testFile = fileRepository.save(testFile);
        assertThat(testFile).isNotNull();
        assertEquals("Test_File.pdf", testFile.getName());
    }

    @Test
    public void testUUIDNotNull() {
        testFile = fileRepository.save(testFile);
        assertNotNull(testFile.getUuid());
    }

    @Test
    public void testNameUnique() {
        File file1 = new File("uniqueName.pdf");
        fileRepository.save(file1);

        File file2 = new File("uniqueName.pdf");
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            fileRepository.save(file2);
        });

        String expectedMessage = "could not execute statement";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testNameConstraints() {
        Set<ConstraintViolation<File>> violation1 = validator.validate(new File(""));
        assertConstraintViolation(violation1, "name", NotBlank.class);

        Set<ConstraintViolation<File>> violation2 = validator.validate(new File("a".repeat(256)));
        assertConstraintViolation(violation2, "name", Size.class);
    }


    @Test
    public void testFileSave() throws IOException {
        Path filePath = Paths.get(storagePath, "testFile");

        // Simulate saving the file
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);

        // Check if the file exists
        assertTrue(Files.exists(filePath));

        // Clean up
        Files.deleteIfExists(filePath);
    }

}
