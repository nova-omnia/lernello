package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.blocks.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {
}
