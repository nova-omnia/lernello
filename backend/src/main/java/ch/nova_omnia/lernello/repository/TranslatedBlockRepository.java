package ch.nova_omnia.lernello.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.block.TranslatedBlock;

public interface TranslatedBlockRepository extends JpaRepository<TranslatedBlock, UUID> {
    List<TranslatedBlock> findByOriginalBlock(Block originalBlock);
}
