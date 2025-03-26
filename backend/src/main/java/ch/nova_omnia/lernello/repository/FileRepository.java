package ch.nova_omnia.lernello.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.File;

public interface FileRepository extends JpaRepository<File, UUID> {
    Optional<File> findByName(String name);
}