package ch.nova_omnia.lernello.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.LearningUnit;

@Repository
public interface LearningUnitRepository extends JpaRepository<LearningUnit, UUID> {
    /**
     * Find all learning units belonging to a learning kit, ordered by position
     *
     * @param learningKitId The ID of the learning kit
     * @return List of learning units in position order
     */
    @Query("SELECT lu FROM LearningUnit lu WHERE lu.learningKit.uuid = :learningKitId ORDER BY lu.position ASC")
    List<LearningUnit> findLearningUnitAsc(UUID learningKitId);

    /**
     * Update the position of a specific learning unit
     *
     * @param position The new position
     * @param learningUnitId  The Learning Unit ID
     */
    @Modifying
    @Query("UPDATE LearningUnit lu SET lu.position = :position WHERE lu.uuid = :learningUnitId")
    void updatePosition(int position, UUID learningUnitId);
}
