package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.dto.request.UpdateLearningUnitOrderDTO;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.LearningUnit;
import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.TheoryBlock;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;
import ch.nova_omnia.lernello.model.data.progress.LearningUnitProgress;
import ch.nova_omnia.lernello.repository.BlockProgressRepository;
import ch.nova_omnia.lernello.repository.BlockRepository;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import ch.nova_omnia.lernello.repository.LearningUnitProgressRepository;
import ch.nova_omnia.lernello.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.repository.TranslatedBlockRepository;
import ch.nova_omnia.lernello.service.block.AIBlockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningUnitServiceTest {

    @Mock
    private LearningUnitRepository learningUnitRepository;
    @Mock
    private LearningKitRepository learningKitRepository;
    @Mock
    private LearningUnitProgressRepository learningUnitProgressRepository;
    @Mock
    private TranslatedBlockRepository translatedBlockRepository;
    @Mock
    private BlockRepository blockRepository;
    @Mock
    private AIBlockService aiBlockService;
    @Mock
    private BlockProgressRepository blockProgressRepository;
    @InjectMocks
    private LearningUnitService service;

    /**
     * Test that createLearningUnit sets position based on existing units and saves it.
     */
    @Test
    void shouldCreateLearningUnitWithCorrectPosition() {
        LearningUnit unit = new LearningUnit();
        UUID kitId = UUID.randomUUID();
        LearningKit kit = new LearningKit();
        kit.setLearningUnits(new ArrayList<>(List.of(new LearningUnit(), new LearningUnit())));
        when(learningKitRepository.findById(kitId)).thenReturn(Optional.of(kit));
        when(learningUnitRepository.save(unit)).thenReturn(unit);

        LearningUnit result = service.createLearningUnit(unit, kitId);

        assertEquals(2, result.getPosition());
        verify(learningUnitRepository).save(unit);
    }

    /**
     * Test that findById returns an Optional of LearningUnit when present.
     */
    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        when(learningUnitRepository.findById(id)).thenReturn(Optional.of(unit));

        Optional<LearningUnit> result = service.findById(id);

        assertTrue(result.isPresent());
        assertSame(unit, result.get());
    }

    /**
     * Test that findAll returns all learning units.
     */
    @Test
    void shouldFindAllLearningUnits() {
        List<LearningUnit> units = List.of(new LearningUnit(), new LearningUnit());
        when(learningUnitRepository.findAll()).thenReturn(units);

        List<LearningUnit> result = service.findAll();

        assertEquals(units, result);
    }

    /**
     * Test that updateLearningUnitPosition updates positions in repository in given order.
     */
    @Test
    void shouldUpdateLearningUnitPosition() {
        UUID kitId = UUID.randomUUID();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        LearningUnit lu1 = new LearningUnit();
        lu1.setUuid(id1);
        LearningUnit lu2 = new LearningUnit();
        lu2.setUuid(id2);
        when(learningUnitRepository.findLearningUnitAsc(kitId)).thenReturn(List.of(lu1, lu2));
        UpdateLearningUnitOrderDTO dto = mock(UpdateLearningUnitOrderDTO.class);
        when(dto.learningUnitUuidsInOrder()).thenReturn(List.of(id2, id1));

        service.updateLearningUnitPosition(kitId, dto);

        ArgumentCaptor<Integer> posCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(learningUnitRepository, times(2)).updatePosition(posCaptor.capture(), uuidCaptor.capture());
        List<Integer> positions = posCaptor.getAllValues();
        List<UUID> uuids = uuidCaptor.getAllValues();
        assertEquals(List.of(0, 1), positions);
        assertEquals(List.of(id2, id1), uuids);
    }

    /**
     * Test that updateLearningUnitPosition throws when a unit ID is not found.
     */
    @Test
    void shouldThrowWhenUpdatingPositionWithInvalidId() {
        UUID kitId = UUID.randomUUID();
        UUID missingId = UUID.randomUUID();
        LearningUnit lu = new LearningUnit();
        lu.setUuid(UUID.randomUUID());
        when(learningUnitRepository.findLearningUnitAsc(kitId)).thenReturn(List.of(lu));
        UpdateLearningUnitOrderDTO dto = mock(UpdateLearningUnitOrderDTO.class);
        when(dto.learningUnitUuidsInOrder()).thenReturn(List.of(missingId));

        assertThrows(RuntimeException.class, () -> service.updateLearningUnitPosition(kitId, dto));
    }

    /**
     * Test that renameLearningUnit saves renamed unit when name is valid.
     */
    @Test
    void shouldRenameLearningUnit() {
        UUID id = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        when(learningUnitRepository.findById(id)).thenReturn(Optional.of(unit));
        when(learningUnitRepository.save(unit)).thenReturn(unit);

        LearningUnit result = service.renameLearningUnit(id, "New Name");

        assertEquals("New Name", result.getName());
        verify(learningUnitRepository).save(unit);
    }

    /**
     * Test that renameLearningUnit throws when name is null or blank.
     */
    @Test
    void shouldThrowOnRenameWhenNameInvalid() {
        UUID id = UUID.randomUUID();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.renameLearningUnit(id, null)), () -> assertThrows(IllegalArgumentException.class, () -> service.renameLearningUnit(id, "  "))
        );
    }

    /**
     * Test that generateLearningUnitWithAI replaces blocks and saves the unit.
     */
    @Test
    void shouldGenerateLearningUnitWithAI() {
        UUID unitId = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        unit.setUuid(unitId);
        Block old1 = new TheoryBlock();
        Block old2 = new TheoryBlock();
        unit.setBlocks(new ArrayList<>(List.of(old1, old2)));
        TranslatedBlock tb1 = new TranslatedBlock();
        TranslatedBlock tb2 = new TranslatedBlock();
        when(learningUnitRepository.findById(unitId)).thenReturn(Optional.of(unit));
        when(translatedBlockRepository.findByOriginalBlockIn(List.of(old1, old2))).thenReturn(List.of(tb1, tb2));
        List<UUID> fileIds = List.of(UUID.randomUUID());
        when(aiBlockService.generateBlocksAI(fileIds)).thenReturn(List.of(new TheoryBlock(), new TheoryBlock()));
        when(learningUnitRepository.save(unit)).thenReturn(unit);

        LearningUnit result = service.generateLearningUnitWithAI(fileIds, unitId);

        assertEquals(2, result.getBlocks().size());
        verify(translatedBlockRepository).deleteAll(List.of(tb1, tb2));
        verify(blockRepository).deleteAll(List.of(old1, old2));
        verify(aiBlockService).generateBlocksAI(fileIds);
        verify(learningUnitRepository).save(unit);
    }

    /**
     * Test that generateLearningUnitWithAI throws when learning unit is not found.
     */
    @Test
    void shouldThrowWhenGenerateLearningUnitWithAINotFound() {
        UUID unitId = UUID.randomUUID();
        when(learningUnitRepository.findById(unitId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.generateLearningUnitWithAI(List.of(), unitId));
    }

    /**
     * Test that deleteById deletes unit and its progress.
     */
    @Test
    void shouldDeleteLearningUnit() {
        UUID id = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        unit.setUuid(id);
        when(learningUnitProgressRepository.findAllByLearningUnit_Uuid(id)).thenReturn(List.of(new LearningUnitProgress()));

        service.deleteById(id);

        verify(learningUnitProgressRepository).deleteAll(List.of(new LearningUnitProgress()));
        verify(learningUnitRepository).deleteById(id);
    }
}

