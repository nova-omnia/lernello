package ch.nova_omnia.lernello.service.test;

import ch.nova_omnia.lernello.model.data.File;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.FileRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.service.file.FileSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

    @Mock
    private FileRepository fileRepo;

    @Mock
    private LearningKitRepository kitRepo;

    @InjectMocks
    private FileSystemService fileService;

    @TempDir
    Path tempStorage;

    @BeforeEach
    void setup() {
        // Point FileSystemService to our temp directory
        ReflectionTestUtils.setField(fileService, "storagePath", tempStorage.toString());
    }

    @Test
    void save_shouldResolveDuplicateNames() throws IOException {
        String originalName = "x.pdf";
        String duplicateName = "x (1).pdf";
        // Stub findByName to simulate name collision then uniqueness
        File existing = new File(originalName);
        when(fileRepo.findByName(originalName)).thenReturn(Optional.of(existing));
        when(fileRepo.findByName(duplicateName)).thenReturn(Optional.empty());
        // Stub save to assign UUID and return the entity
        when(fileRepo.save(any(File.class))).thenAnswer(inv -> {
            File f = inv.getArgument(0);
            f.setUuid(UUID.randomUUID());
            return f;
        });

        // Act: save a mock upload
        File saved = fileService.save(
            new MockMultipartFile("file", originalName, "application/pdf", "test".getBytes())
        );

        // Assert: name was adjusted and file exists on disk
        assertThat(saved.getName()).isEqualTo(duplicateName);
        assertThat(Files.exists(tempStorage.resolve(saved.getUuid().toString()))).isTrue();
    }

    @Test
    void extractTextFromFiles_shouldReturnConcatenatedText() throws IOException {
        UUID fileId = UUID.randomUUID();
        File entity = new File("pdf.pdf");
        entity.setUuid(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(entity));

        // Create a simple PDF with known text
        java.io.File pdfFile = tempStorage.resolve(fileId.toString()).toFile();
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(100, 700);
        cs.showText("Hello Test");
        cs.endText();
        cs.close();
        doc.save(pdfFile);
        doc.close();

        // Act
        String text = fileService.extractTextFromFiles(List.of(fileId));

        // Assert
        assertThat(text).contains("Hello Test");
    }
}
