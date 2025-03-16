package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.Folder;

public interface FolderRepository extends JpaRepository<Folder, UUID> {
}
