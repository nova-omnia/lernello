package ch.nova_omnia.pm4.repository;

import ch.nova_omnia.pm4.model.data.Folder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}