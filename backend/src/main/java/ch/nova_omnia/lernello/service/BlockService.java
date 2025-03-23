package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.repository.BlockRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public class BlockService {
    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Transactional
    public void deleteBlock(UUID id) {
        blockRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBlocks() {
        blockRepository.deleteAll();
    }
}
