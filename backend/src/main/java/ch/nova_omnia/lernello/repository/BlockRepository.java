package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.blocks.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

    /**
     * Find all blocks belonging to a learning unit, ordered by position
     * @param learningUnitId The ID of the learning unit
     * @return List of blocks in position order
     */
    List<Block> findByLearningUnitIdOrderByPositionAsc(UUID learningUnitId);

    /**
     * Check if a block exists with the given name in a learning unit
     * @param name The block name
     * @param learningUnitId The learning unit ID
     * @return true if exists
     */
    boolean existsByNameAndLearningUnitId(String name, UUID learningUnitId);

    /**
     * Update the position of a specific block
     * @param position The new position
     * @param blockId The block ID
     */
    @Modifying
    @Query("UPDATE Block b SET b.position = :position WHERE b.id = :blockId")
    void updatePosition(int position, UUID blockId);

    /**
     * Count blocks in a learning unit
     * @param learningUnitId The learning unit ID
     * @return number of blocks
     */
    long countByLearningUnitId(UUID learningUnitId);
}