package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningKitServiceTest {

    @Mock
    private LearningKitRepository learningKitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private LearningKitService service;

    /**
     * Test that getList returns published kits for a trainee user
     */
    @Test
    void shouldReturnPublishedKitsForTrainee() {
        UUID traineeId = UUID.randomUUID();
        when(userRepository.findByUuid(traineeId)).thenReturn(new User("username", "surname", "name", "password", "en", Role.TRAINEE));
        List<LearningKit> kits = List.of(new LearningKit(), new LearningKit());
        Page<LearningKit> page = new PageImpl<>(kits);
        when(learningKitRepository.findAllByTrainees_UuidAndPublishedTrue(traineeId, Pageable.unpaged())).thenReturn(page);

        Page<LearningKit> result = service.getList(Pageable.unpaged(), traineeId);

        assertEquals(kits, result.getContent());
    }

    /**
     * Test that getList returns all kits for a non-trainee user ordered by creation date descending
     */
    @Test
    void shouldReturnAllKitsForNonTrainee() {
        UUID adminId = UUID.randomUUID();
        when(userRepository.findByUuid(adminId)).thenReturn(new User("username", "surname", "name", "password", "en", Role.INSTRUCTOR));
        when(learningKitRepository.findAllByOrderByCreatedAtDesc(Pageable.unpaged())).thenReturn(Page.empty());
        List<LearningKit> kits = List.of(new LearningKit());
        Page<LearningKit> page = new PageImpl<>(kits);
        when(learningKitRepository.findAllByOrderByCreatedAtDesc(Pageable.unpaged())).thenReturn(page);

        Page<LearningKit> result = service.getList(Pageable.unpaged(), adminId);

        assertEquals(kits, result.getContent());
    }

    /**
     * Test that findById throws EntityNotFoundException when kit is not found
     */
    @Test
    void shouldThrowWhenFindingNonexistentById() {
        UUID missingId = UUID.randomUUID();
        when(learningKitRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(missingId));
    }

    /**
     * Test that save delegates to repository save
     */
    @Test
    void shouldSaveLearningKit() {
        LearningKit kit = new LearningKit();
        when(learningKitRepository.save(kit)).thenReturn(kit);

        LearningKit result = service.save(kit);

        assertSame(kit, result);
    }

    /**
     * Test that deleteById delegates to repository deleteById
     */
    @Test
    void shouldDeleteById() {
        UUID kitId = UUID.randomUUID();

        service.deleteById(kitId);

        verify(learningKitRepository).deleteById(kitId);
    }

    /**
     * Test that removeTrainee removes an existing trainee and saves the kit
     */
    @Test
    void shouldRemoveExistingTrainee() {
        UUID kitId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User trainee = new User("trainee", "surname", "name", "password", "en", Role.TRAINEE);
        trainee.setUuid(userId);
        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);
        kit.setTrainees(new ArrayList<>(List.of(trainee)));
        when(learningKitRepository.findById(kitId)).thenReturn(Optional.of(kit));

        service.removeTrainee(kitId, userId);

        assertTrue(kit.getTrainees().isEmpty());
        verify(learningKitRepository).save(kit);
    }

    /**
     * Test that removeTrainee throws when the trainee is not in the kit
     */
    @Test
    void shouldThrowWhenRemovingNonexistentTrainee() {
        UUID kitId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LearningKit kit = new LearningKit();
        kit.setTrainees(new ArrayList<>());
        when(learningKitRepository.findById(kitId)).thenReturn(Optional.of(kit));

        assertThrows(IllegalArgumentException.class, () -> service.removeTrainee(kitId, userId));
    }

    /**
     * Test that publishLearningKit sends invitations only to trainees and marks the kit published
     */
    @Test
    void shouldPublishAndInviteOnlyTrainees() {
        UUID kitId = UUID.randomUUID();
        User t1 = new User("trainee", "surname", "name", "password", "en", Role.TRAINEE);
        User t2 = new User("other", "surname", "name", "password", "en", Role.INSTRUCTOR);
        LearningKit kit = new LearningKit();
        kit.setTrainees(new ArrayList<>(List.of(t1, t2)));
        when(learningKitRepository.findById(kitId)).thenReturn(Optional.of(kit));

        service.publishLearningKit(kitId);

        verify(emailService).sendLearningKitInvitation(t1, kit);
        verify(emailService, never()).sendLearningKitInvitation(t2, kit);
        assertTrue(kit.isPublished());
    }

    /**
     * Test that saveTraineeInKit adds a trainee and saves the kit
     */
    @Test
    void shouldSaveTraineeInKit() {
        UUID kitId = UUID.randomUUID();
        User newTrainee = new User("newTrainee", "surname", "name", "password", "en", Role.TRAINEE);
        LearningKit kit = new LearningKit();
        kit.setTrainees(new ArrayList<>());
        when(learningKitRepository.findById(kitId)).thenReturn(Optional.of(kit));

        service.saveTraineeInKit(kitId, newTrainee);

        assertTrue(kit.getTrainees().contains(newTrainee));
        verify(learningKitRepository).save(kit);
    }

    /**
     * Test that reorderLearningUnits completes without error
     */
    @Test
    void shouldDoNothingWhenReorderingLearningUnits() {
        LearningKit kit = new LearningKit();

        assertDoesNotThrow(() -> service.reorderLearningUnits(kit, new String[]{"a", "b"}));
    }
}
