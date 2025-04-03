package ch.nova_omnia.lernello.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.block.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

    /**
     * Find all blocks belonging to a learning unit, ordered by position
     * 
     * @param learningUnitId The ID of the learning unit
     * @return List of blocks in position order
     */
    @Query("SELECT b FROM Block b WHERE b.learningUnit.uuid = :learningUnitUuid ORDER BY b.position ASC")
    List<Block> findBlocksAsc(UUID learningUnitUuid);

    /**
     * Update the position of a specific block
     * 
     * @param position The new position
     * @param blockId  The block ID
     */
    @Modifying
    @Query("UPDATE Block b SET b.position = :position WHERE b.id = :blockId")
    void updatePosition(int position, UUID blockId);
}