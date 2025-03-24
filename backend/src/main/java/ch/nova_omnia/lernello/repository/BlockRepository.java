package ch.nova_omnia.lernello.repository;

import ch.nova_omnia.lernello.model.data.blocks.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, UUID> {
}
