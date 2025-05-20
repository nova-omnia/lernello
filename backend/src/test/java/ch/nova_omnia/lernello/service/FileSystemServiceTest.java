package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.File;
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
    private LearningKitRepository learningKitRepo;

    @InjectMocks
    private FileSystemService fileService;

    @TempDir
    Path tempStorage;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(fileService, "storagePath", tempStorage.toString());
    }

    /**
     * Tests if a file is saved correctly and the UUID is set.
     */
    @Test
    void save_shouldResolveDuplicateNames() {
        String originalName = "x.pdf";
        String duplicateName = "x (1).pdf";
        File existing = new File(originalName);
        when(fileRepo.findByName(originalName)).thenReturn(Optional.of(existing));
        when(fileRepo.findByName(duplicateName)).thenReturn(Optional.empty());
        when(fileRepo.save(any(File.class))).thenAnswer(inv -> {
            File f = inv.getArgument(0);
            f.setUuid(UUID.randomUUID());
            return f;
        });

        File saved = fileService.save(
            new MockMultipartFile("file", originalName, "application/pdf", "test".getBytes())
        );

        assertThat(saved.getName()).isEqualTo(duplicateName);
        assertThat(Files.exists(tempStorage.resolve(saved.getUuid().toString()))).isTrue();
    }

    /**
     * Tests that if a file is deleted but not found in the repository, an exception is thrown.
     */
    @Test
    void deleteById_shouldThrowIfNotFound() {
        UUID missingId = UUID.randomUUID();
        when(fileRepo.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.deleteById(missingId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("not found");
    }

    /**
     * Tests if the extracted text from a PDF file is correct.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    void extractTextFromFiles_shouldReturnConcatenatedText() throws IOException {
        UUID fileId = UUID.randomUUID();
        File entity = new File("pdf.pdf");
        entity.setUuid(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(entity));

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

        String text = fileService.extractTextFromFiles(List.of(fileId));

        assertThat(text).contains("Hello Test");
    }

    /**
     * Tests if a file is deleted correctly when it exists.
     */
    @Test
    void deleteById_shouldDeleteFileIfExists() {
        UUID fileId = UUID.randomUUID();
        File file = new File("test.pdf");
        file.setUuid(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));

        fileService.deleteById(fileId);

        verify(fileRepo, times(1)).deleteById(fileId);
    }

    /**
         * Tests if all files are returned correctly.
         */
        @Test
        void findAll_shouldReturnAllFiles() {
            List<File> files = List.of(new File("file1.pdf"), new File("file2.pdf"));
            when(fileRepo.findAll()).thenReturn(files);

            List<File> result = fileService.findAll();

            assertThat(result).isEqualTo(files);
        }

        /**
         * Tests if files are returned correctly by their IDs.
         */
        @Test
        void findAllByIds_shouldReturnFilesByIds() {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();
            List<File> files = List.of(new File("file1.pdf"), new File("file2.pdf"));
            when(fileRepo.findAllById(List.of(id1, id2))).thenReturn(files);

            List<File> result = fileService.findAllByIds(List.of(id1, id2));

            assertThat(result).isEqualTo(files);
        }

        /**
         * Tests if null is returned when the input list of IDs is null.
         */
        @Test
        void findAllByIds_shouldReturnNullIfInputIsNull() {
            List<File> result = fileService.findAllByIds(null);

            assertThat(result).isNull();
        }

        /**
         * Tests if a file is returned correctly by its ID.
         */
        @Test
        void findById_shouldReturnFileById() {
            UUID fileId = UUID.randomUUID();
            File file = new File("file.pdf");
            file.setUuid(fileId);
            when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));

            Optional<File> result = fileService.findById(fileId);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(file);
        }

        /**
         * Tests if an empty optional is returned when the file is not found by its ID.
         */
        @Test
        void findById_shouldReturnEmptyIfFileNotFound() {
            UUID fileId = UUID.randomUUID();
            when(fileRepo.findById(fileId)).thenReturn(Optional.empty());

            Optional<File> result = fileService.findById(fileId);

            assertThat(result).isEmpty();
        }
}
