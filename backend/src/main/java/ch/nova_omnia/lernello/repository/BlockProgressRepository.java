package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.block.Block;
import ch.nova_omnia.lernello.model.data.progress.block.BlockProgress;
import ch.nova_omnia.lernello.model.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockProgressRepository extends JpaRepository<BlockProgress, UUID> {
    Optional<BlockProgress> findByUserAndBlock(User user, Block block);

    Optional<BlockProgress> findByUser_UuidAndBlock_Uuid(UUID userId, UUID blockId);

    List<BlockProgress> findByBlock_Uuid(UUID blockId);

    List<BlockProgress> findByLearningUnitProgress_Uuid(UUID learningUnitProgressUuid);

    void deleteByBlockIn(List<Block> blocks);
}
